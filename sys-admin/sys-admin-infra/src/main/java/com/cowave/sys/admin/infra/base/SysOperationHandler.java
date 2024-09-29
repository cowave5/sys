/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.base;

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationHandler;
import com.cowave.commons.framework.access.operation.OperationInfo;
import com.cowave.commons.framework.helper.es.EsHelper;
import com.cowave.sys.admin.domain.base.SysOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.settings.Settings;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author shanhuiming
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SysOperationHandler implements OperationHandler {

    private static final String mappingProperties = """
            {
                "mappings": {
                    "properties": {
                        "opTime": {
                            "type": "date",
                            "format": "yyyy-MM-dd HH:mm:ss||epoch_millis"
                        }
                    }
                }
            }
            """;

    private final EsHelper esHelper;

    @PostConstruct
    public void indexInit(){
        esHelper.indexCreate(SysOperation.INDEX_NAME, mappingProperties);
        esHelper.indexSetting(SysOperation.INDEX_NAME, Settings.builder().put("index.max_result_window", 25000));
    }

    @Override
    public void handle(OperationInfo opInfo, Map<String, Object> args, Object resp, Exception e) {
        SysOperation sysOperation = new SysOperation(opInfo);
        sysOperation.setAccess(Access.accessInfo());
        sysOperation.setIp(Access.accessIp());
        sysOperation.setUrl(Access.accessMethod() + " " + Access.accessUrl());
        sysOperation.setOpTime(Access.accessTime());
        // 请求内容
        sysOperation.setRequest(Access.getRequestParam());
        // 响应内容
        sysOperation.setResponse(resp);
        esHelper.insert(SysOperation.INDEX_NAME, sysOperation);
    }
}
