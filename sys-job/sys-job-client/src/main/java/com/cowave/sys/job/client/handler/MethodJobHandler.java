package com.cowave.sys.job.client.handler;

import com.cowave.sys.job.client.JobContent;
import com.cowave.sys.job.domain.constant.TriggerStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author xuxueli/shanhuiming
 */
@Slf4j
@RequiredArgsConstructor
public class MethodJobHandler extends JobHandler {

    private final Object target;

    private final Method method;

    private final Method initMethod;

    private final Method destroyMethod;

    @Override
    public void execute() {
        long startTime = System.currentTimeMillis();
        JobContent.get().setHandleTime(new Date());
        Class<?>[] paramTypes = method.getParameterTypes();
        try {
            if (paramTypes.length > 0) {
                method.invoke(target, new Object[paramTypes.length]);
            } else {
                method.invoke(target);
            }
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
        if(initMethod != null) {
            initMethod.invoke(target);
        }
    }

    @Override
    public void destroy() throws Exception {
        if(destroyMethod != null) {
            destroyMethod.invoke(target);
        }
    }
}
