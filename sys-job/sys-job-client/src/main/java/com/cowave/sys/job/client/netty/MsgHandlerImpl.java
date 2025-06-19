package com.cowave.sys.job.client.netty;

import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.job.client.*;
import com.cowave.sys.job.client.handler.GlueJobHandler;
import com.cowave.sys.job.client.handler.JobHandler;
import com.cowave.sys.job.client.handler.ScriptJobHandler;
import com.cowave.sys.job.domain.constant.BlockStrategyEnum;
import com.cowave.sys.job.domain.constant.GlueTypeEnum;
import com.cowave.sys.job.domain.request.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xuxueli/shanhuiming
 */
@Slf4j
public class MsgHandlerImpl implements MsgHandler {

    @Override
    public Response<Void> beat() {
        return Response.success();
    }

    @Override
    public Response<Void> checkIdle(IdleCheck idleCheck) {
        JobThread jobThread = JobContext.getJobThread(idleCheck.getJobId());
        if (jobThread != null && jobThread.isRunningOrHasQueue()) {
            return Response.error("client is busy");
        }
        return Response.success();
    }

    @Override
    public Response<String> exec(TriggerRequest triggerRequest) {
        // triggerId -> JobThread
        JobThread jobThread = JobContext.getJobThread(triggerRequest.getTriggerId());
        // 上一次的处理
        JobHandler lastHandler = jobThread != null ? jobThread.getJobHandler() : null;

        // Spring Bean处理
        GlueTypeEnum glueTypeEnum = GlueTypeEnum.match(triggerRequest.getGlueType());
        if (GlueTypeEnum.BEAN == glueTypeEnum) {
            JobHandler currentHandler = JobContext.getJobHandler(triggerRequest.getHandlerName());
            // jobHandler变化
            if (jobThread != null && lastHandler != currentHandler) {
                jobThread = null;
            }

            lastHandler = currentHandler;
            if (lastHandler == null) {
                return Response.error("handler[" + triggerRequest.getHandlerName() + "] not found");
            }
        } else if (GlueTypeEnum.GLUE_GROOVY == glueTypeEnum) {
            // jobHandler变化
            if (jobThread != null &&
                    !(jobThread.getJobHandler() instanceof GlueJobHandler
                        && ((GlueJobHandler) jobThread.getJobHandler()).getGlueUpdatetime() == triggerRequest.getGlueUpdateTime() )) {
                jobThread = null;
                lastHandler = null;
            }

            // valid handler
            if (lastHandler == null) {
                try {
                    JobHandler originJobHandler = GlueFactory.instance.loadNewInstance(triggerRequest.getGlueSource());
                    lastHandler = new GlueJobHandler(originJobHandler, triggerRequest.getGlueUpdateTime());
                } catch (Exception e) {
                    log.error("", e);
                    return Response.error(e.getMessage());
                }
            }
        } else if (glueTypeEnum != null && glueTypeEnum.isScript()) {
            if (jobThread != null &&
                    !(jobThread.getJobHandler() instanceof ScriptJobHandler
                            && ((ScriptJobHandler) jobThread.getJobHandler()).getGlueUpdatetime() == triggerRequest.getGlueUpdateTime())) {
                jobThread = null;
                lastHandler = null;
            }

            // valid handler
            if (lastHandler == null) {
                lastHandler = new ScriptJobHandler(triggerRequest.getTriggerId(), triggerRequest.getGlueUpdateTime(), triggerRequest.getGlueSource(), GlueTypeEnum.match(triggerRequest.getGlueType()));
            }
        } else {
            return Response.error("invalid glueType[" + triggerRequest.getGlueType() + "]");
        }

        // executor block strategy
        if (jobThread != null) {
            BlockStrategyEnum blockStrategy = BlockStrategyEnum.match(triggerRequest.getBlockStrategy(), null);
            if (BlockStrategyEnum.DISCARD_LATER == blockStrategy) {
                // discard when running
                if (jobThread.isRunningOrHasQueue()) {
                    return Response.error("discard when block");
                }
            } else if (BlockStrategyEnum.COVER_EARLY == blockStrategy) {
                // kill running jobThread
                if (jobThread.isRunningOrHasQueue()) {
                    jobThread = null;
                }
            } else {
                // just queue trigger
            }
        }

        // replace thread (new or exists invalid)
        if (jobThread == null) {
            jobThread = JobContext.putJobThread(triggerRequest.getTriggerId(), lastHandler);
        }
        return jobThread.pushTriggerQueue(triggerRequest);
    }

    @Override
    public Response<Void> kill(KillRequest killRequest) {
        // kill handlerThread, and create new one
        JobThread jobThread = JobContext.getJobThread(killRequest.getJobId());
        if (jobThread != null) {
            JobContext.delJobThread(killRequest.getJobId());
            return Response.success();
        }
        return Response.error("client already killed.");
    }
}
