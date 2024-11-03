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

import com.cowave.commons.framework.access.operation.OperationHandler;
import com.cowave.commons.framework.access.operation.OperationInfo;
import com.cowave.commons.response.Response;
import com.cowave.sys.admin.core.service.SysLogService;
import com.cowave.sys.admin.core.entity.dto.SysLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class SysOperationHandler implements OperationHandler {
    public static final String ALL = "all";
    public static final String REQ = "req";
    public static final String RESP = "resp";

    private final SysLogService sysLogService;

    @Override
    public void handle(OperationInfo operationInfo, Map<String, Object> args, Object resp, Exception e) {
        SysLogDto sysLog = new SysLogDto(operationInfo);
        String opFlag = operationInfo.getOpFlag();
        if (REQ.equals(opFlag)) {
            appendRequest(sysLog, args);
        }else if(RESP.equals(opFlag)){
            appendResponse(sysLog, resp);
        }else if(ALL.equals(opFlag)){
            appendRequest(sysLog, args);
            appendResponse(sysLog, resp);
        }
        sysLogService.add(sysLog);
    }

    private void appendRequest(SysLogDto sysLog, Map<String, Object> args) {
        if (args.size() == 1) {
            sysLog.putContent("req", args.values().iterator().next());
        } else {
            sysLog.putContent("req", args);
        }
    }

    private void appendResponse(SysLogDto sysLog, Object resp) {
        if (resp instanceof Response<?> response) {
            sysLog.putContent("resp", response.getData());
        } else {
            sysLog.putContent("resp", resp);
        }
    }
}
