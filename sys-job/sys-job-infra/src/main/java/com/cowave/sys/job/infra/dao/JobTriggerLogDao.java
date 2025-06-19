package com.cowave.sys.job.infra.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.job.domain.JobTriggerLog;
import com.cowave.sys.job.domain.request.TriggerResponse;
import com.cowave.sys.job.infra.dao.mapper.JobTriggerLogMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xuxueli/shanhuiming
 */
@Repository
public class JobTriggerLogDao extends ServiceImpl<JobTriggerLogMapper, JobTriggerLog> {

    public void completeTriggerLog(List<TriggerResponse> list) {
        for (TriggerResponse triggerResponse : list) {
            lambdaUpdate()
                    .eq(JobTriggerLog::getTriggerId, triggerResponse.getTriggerId())
                    .eq(JobTriggerLog::getId, triggerResponse.getLogId())
                    .set(JobTriggerLog::getTriggerStatus, triggerResponse.getHandleStatus())
                    .set(JobTriggerLog::getHandleTime, triggerResponse.getHandleTime())
                    .set(JobTriggerLog::getHandleCost, triggerResponse.getHandleCost())
                    .set(JobTriggerLog::getFailMsg, triggerResponse.getHandleMsg())
                    .update();
        }
    }
}
