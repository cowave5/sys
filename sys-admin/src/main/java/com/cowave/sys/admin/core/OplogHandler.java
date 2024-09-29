/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.core;

import com.cowave.commons.framework.helper.operation.OperationInfo;
import com.cowave.sys.admin.api.service.SysLogService;
import com.cowave.sys.model.admin.SysLog;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Component
public class OplogHandler {

    private final SysLogService sysLogService;

    public void log(OperationInfo opInfo){
        SysLog sysLog = new SysLog(opInfo);
        sysLogService.add(sysLog);
    }

    public void logRequest(OperationInfo opInfo){
        SysLog sysLog = new SysLog(opInfo);
        Map<String, Object> opArgs = opInfo.getOpArgs();
        if(opArgs.size() == 1){
            sysLog.putContent("req", opArgs.values().iterator().next());
        }else{
            sysLog.putContent("req", opArgs);
        }
        sysLogService.add(sysLog);
    }

    public void logResponse(OperationInfo opInfo, Response<?> response){
        SysLog sysLog = new SysLog(opInfo);
        sysLog.putContent("resp", response.getData());
        sysLogService.add(sysLog);
    }

    public void logAll(OperationInfo opInfo, Object request, Response<?> response){
        SysLog sysLog = new SysLog(opInfo);
        sysLog.putContent("resp", response.getData());
        Map<String, Object> opArgs = opInfo.getOpArgs();
        if(opArgs.size() == 1){
            sysLog.putContent("req", opArgs.values().iterator().next());
        }else{
            sysLog.putContent("req", opArgs);
        }
        sysLogService.add(sysLog);
    }
}
