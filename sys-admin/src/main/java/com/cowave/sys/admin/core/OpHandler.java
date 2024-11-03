/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core;

import com.cowave.commons.framework.access.operation.OperationInfo;
import com.cowave.commons.response.Response;
import com.cowave.sys.admin.service.sys.SysLogService;
import com.cowave.sys.model.admin.SysLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Component
public class OpHandler {

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
