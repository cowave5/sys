/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.core;

import com.alibaba.fastjson.JSON;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.AccessExceptionHandler;
import com.cowave.commons.framework.configuration.ApplicationConfiguration;
import com.cowave.sys.admin.api.service.SysAlarmService;
import com.cowave.sys.model.admin.SysAlarm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.feign.codec.Response;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求告警
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class AccessAlarmHandler implements AccessExceptionHandler {

    private final ApplicationConfiguration applicationConfiguration;

    private final SysAlarmService sysAlarmService;

    @Override
    public void handler(Exception e, int status, Response<Void> response) {
        Access access = Access.get();
        Map<String, Object> content = new HashMap<>();
        content.put("requestId", access.getAccessId());
        content.put("requestUrl", access.getAccessUrl());
        content.put("requestParam", access.getRequestParam());
        content.put("responseCode", response.getCode());
        content.put("responseMsg", response.getMsg());
        content.put("responseData", response.getData());

        SysAlarm sysAlarm = new SysAlarm();
        String group = applicationConfiguration.getName();
        String type = "access_failed";

        String param = "";
        if(access.getRequestParam() != null){
            param = JSON.toJSONString(access.getRequestParam());
        }
        String md5 = DigestUtils.md5Hex(access.getAccessUrl() + param);
        sysAlarm.recordAlarm(md5, group, type, access.getAccessUrl(), content);
        sysAlarm.setAlarmLevel(3);
        sysAlarmService.add(sysAlarm);
    }
}
