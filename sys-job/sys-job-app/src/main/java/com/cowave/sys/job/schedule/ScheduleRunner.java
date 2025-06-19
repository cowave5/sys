package com.cowave.sys.job.schedule;

import com.cowave.sys.job.domain.JobTrigger;
import com.cowave.sys.job.infra.dao.JobTriggerDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author xuxueli/shanhuiming
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleRunner implements ApplicationRunner {
    private static final Map<Integer, Set<Integer>> ringData = new ConcurrentHashMap<>();
    private static final long PRE_READ_MS = 5000;
    private volatile boolean scheduleThreadToStop = false;
    private volatile boolean ringThreadToStop = false;
    private Thread scheduleThread;
    private Thread ringThread;
    private final ScheduleTriggerExecutor scheduleTriggerExecutor;
    private final JobTriggerDao jobTriggerDao;

    @Override
    public void run(ApplicationArguments args) {
        start();
    }

    public void start() {
        scheduleThread = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(PRE_READ_MS - System.currentTimeMillis() % 1000);
            } catch (Throwable e) {
                if (!scheduleThreadToStop) {
                    log.error("", e);
                }
            }

            // pre-read count: treadpool-size * trigger-qps (each trigger cost 50ms, qps = 1000/50 = 20)
            int preReadCount = 20 * 20;
            while (!scheduleThreadToStop) {
                try {
                    // 1、pre read
                    long nowTime = System.currentTimeMillis();
                    List<JobTrigger> triggerList = jobTriggerDao.preListInSeconds(nowTime + PRE_READ_MS, preReadCount);
                    if (!triggerList.isEmpty()) {
                        for (JobTrigger trigger : triggerList) {
                            if (nowTime > trigger.getNextTime() + PRE_READ_MS) {
                                // 过期超过5s
                                MisfireStrategyEnum misfireStrategyEnum =
                                        MisfireStrategyEnum.match(trigger.getMisfireStrategy(), MisfireStrategyEnum.DO_NOTHING);
                                if (MisfireStrategyEnum.FIRE_ONCE_NOW == misfireStrategyEnum) {
                                    scheduleTriggerExecutor.addTrigger(trigger.getId(), TriggerTypeEnum.MISFIRE, null, null);
                                }
                                refreshNextValidTime(trigger, new Date());
                            } else if (nowTime > trigger.getNextTime()) {
                                // 过期小于5s
                                scheduleTriggerExecutor.addTrigger(trigger.getId(), TriggerTypeEnum.SCHEDULE, null, null);
                                refreshNextValidTime(trigger, new Date());
                                // next-trigger-time in 5s, pre-read again
                                if (trigger.getStatus() == 1 && nowTime + PRE_READ_MS > trigger.getNextTime()) {
                                    // 1、make ring second
                                    int ringSecond = (int) ((trigger.getNextTime() / 1000) % 60);
                                    // 2、push time ring
                                    pushTimeRing(ringSecond, trigger.getId());
                                    // 3、fresh next
                                    refreshNextValidTime(trigger, new Date(trigger.getNextTime()));
                                }
                            } else {
                                // 未过期
                                // 1、make ring second
                                int ringSecond = (int) ((trigger.getNextTime() / 1000) % 60);
                                // 2、push time ring
                                pushTimeRing(ringSecond, trigger.getId());
                                // 3、fresh next
                                refreshNextValidTime(trigger, new Date(trigger.getNextTime()));
                            }
                        }
                        // 3、update trigger info
                        for (JobTrigger trigger : triggerList) {
                            jobTriggerDao.updateTime(trigger);
                        }
                    }
                    // tx stop
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (Throwable e) {
                    if (!scheduleThreadToStop) {
                        log.error("", e);
                    }
                }
            }
        });
        scheduleThread.setDaemon(true);
        scheduleThread.setName("job-scheduler");
        scheduleThread.start();

        // ring thread
        ringThread = new Thread(() -> {
            int lastSecond = Calendar.getInstance().get(Calendar.SECOND);
            while (!ringThreadToStop) {
                int currentSecond = Calendar.getInstance().get(Calendar.SECOND);
                if (lastSecond != currentSecond) {
                    lastSecond = currentSecond;
                    try {
                        Set<Integer> list = ringData.remove(currentSecond);
                        if (list != null) {
                            for (int jobId : list) {
                                scheduleTriggerExecutor.addTrigger(jobId, TriggerTypeEnum.SCHEDULE, null, null);
                            }
                        }
                    } catch (Throwable e) {
                        if (!ringThreadToStop) {
                            log.error("", e);
                        }
                    }
                } else {
                    try {
                        // 这里有个put/get的先后顺序问题，没有特别好的办法，将get操作往后偏100ms，如果put错过窗口只能下次get
                        TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
                    } catch (Throwable e) {
                        if (!ringThreadToStop) {
                            log.error("", e);
                        }
                    }
                }
            }
        });
        ringThread.setDaemon(true);
        ringThread.setName("job-ring");
        ringThread.start();
    }

    private void refreshNextValidTime(JobTrigger jobTrigger, Date fromTime) {
        try {
            Date nextValidTime = generateNextValidTime(jobTrigger, fromTime);
            if (nextValidTime != null) {
                jobTrigger.setStatus(-1);   // pass, may be Inaccurate
                jobTrigger.setLastTime(jobTrigger.getNextTime());
                jobTrigger.setNextTime(nextValidTime.getTime());
            } else {
                // generateNextValidTime fail, stop job
                jobTrigger.setStatus(0);
                jobTrigger.setLastTime(0);
                jobTrigger.setNextTime(0);
            }
        } catch (Throwable e) {
            jobTrigger.setStatus(0);
            jobTrigger.setLastTime(0);
            jobTrigger.setNextTime(0);
            log.error("", e);
        }
    }

    private void pushTimeRing(int ringSecond, int jobId) {
        Set<Integer> ringItemData = ringData.computeIfAbsent(ringSecond, k -> new HashSet<>());
        ringItemData.add(jobId);
    }

    @PreDestroy
    public void toStop() {
        // 1、stop schedule
        scheduleThreadToStop = true;
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Throwable e) {
            log.error("", e);
        }

        if (scheduleThread.getState() != Thread.State.TERMINATED) {
            scheduleThread.interrupt();
            try {
                scheduleThread.join();
            } catch (Throwable e) {
                log.error("", e);
            }
        }

        // if has ring data
        boolean hasRingData = false;
        if (!ringData.isEmpty()) {
            for (int second : ringData.keySet()) {
                Set<Integer> tmpData = ringData.get(second);
                if (tmpData != null && !tmpData.isEmpty()) {
                    hasRingData = true;
                    break;
                }
            }
        }
        if (hasRingData) {
            try {
                TimeUnit.SECONDS.sleep(8);
            } catch (Throwable e) {
                log.error("", e);
            }
        }

        // stop ring (wait job-in-memory stop)
        ringThreadToStop = true;
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Throwable e) {
            log.error("", e);
        }
        if (ringThread.getState() != Thread.State.TERMINATED) {
            ringThread.interrupt();
            try {
                ringThread.join();
            } catch (Throwable e) {
                log.error("", e);
            }
        }
    }

    public static Date generateNextValidTime(JobTrigger jobInfo, Date fromTime) throws Exception {
        ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(jobInfo.getTriggerType(), null);
        if (ScheduleTypeEnum.CRON == scheduleTypeEnum) {
            return new CronExpression(jobInfo.getTriggerParam()).getNextValidTimeAfter(fromTime);
        } else if (ScheduleTypeEnum.FIX_RATE == scheduleTypeEnum /*|| ScheduleTypeEnum.FIX_DELAY == scheduleTypeEnum*/) {
            return new Date(fromTime.getTime() + Long.parseLong(jobInfo.getTriggerParam()) * 1000L);
        }
        return null;
    }
}
