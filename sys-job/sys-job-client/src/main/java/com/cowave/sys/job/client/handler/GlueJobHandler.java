package com.cowave.sys.job.client.handler;

import com.cowave.sys.job.client.JobContent;
import com.cowave.sys.job.domain.constant.TriggerStatusEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author xuxueli/shanhuiming
 */
@Slf4j
@RequiredArgsConstructor
public class GlueJobHandler extends JobHandler {

    private final JobHandler jobHandler;

    @Getter
    private final long glueUpdatetime;

    @Override
    public void execute() {
        long startTime = System.currentTimeMillis();
        JobContent.get().setHandleTime(new Date());
        try {
            jobHandler.execute();
            JobContent.get().setHandleStatus(TriggerStatusEnum.EXEC_SUCCESS.getStatus());
        } catch (Exception e) {
            log.error("", e);
            JobContent.get().setHandleStatus(TriggerStatusEnum.EXEC_FAIL.getStatus());
            JobContent.get().setHandleMsg(e.getMessage());
        }
        JobContent.get().setHandleCost(System.currentTimeMillis() - startTime);
    }

    @Override
    public void init() throws Exception {
        this.jobHandler.init();
    }

    @Override
    public void destroy() throws Exception {
        this.jobHandler.destroy();
    }
}
