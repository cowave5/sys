/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.base.infra;

import com.cowave.commons.framework.access.operation.OperationHandler;
import com.cowave.commons.framework.access.operation.OperationInfo;
import com.cowave.sys.admin.base.domain.dto.SysLogDto;
import com.cowave.sys.admin.base.service.SysLogService;
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
    public void handle(OperationInfo opInfo, Map<String, Object> args, Object resp, Exception e) {
        SysLogDto sysLog = new SysLogDto(opInfo);
        String opFlag = opInfo.getOpFlag();
        if (REQ.equals(opFlag)) {
            appendRequest(sysLog, args);
        }else if(RESP.equals(opFlag)){
            // 原先记的response换成了content，这里不改了
            sysLog.putContent("resp", opInfo.getOpContent());
        }else if(ALL.equals(opFlag)){
            appendRequest(sysLog, args);
            sysLog.putContent("resp", opInfo.getOpContent());
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
}
