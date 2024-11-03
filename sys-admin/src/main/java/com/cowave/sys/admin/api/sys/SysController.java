/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.api.sys;

import com.cowave.commons.response.Response;
import com.cowave.sys.admin.core.entity.CacheInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 系统接口
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/sys")
public class SysController {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * Redis缓存状态
     */
    @GetMapping(value = "/cache")
    public Response<CacheInfo> cache() {
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::info);
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::dbSize);

        CacheInfo cacheInfo = new CacheInfo();
        cacheInfo.setInfo(info);
        cacheInfo.setDbSize(dbSize);

        assert commandStats != null;
        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", removeStart(key));
            data.put("value", substringBetween(property));
            pieList.add(data);
        });
        cacheInfo.setCommandStats(pieList);
        return Response.success(cacheInfo);
    }

    private static String removeStart(String str) {
        if (StringUtils.isNotBlank(str) && StringUtils.isNotBlank("cmdstat_")) {
            return str.startsWith("cmdstat_") ? str.substring("cmdstat_".length()) : str;
        } else {
            return str;
        }
    }

    private static String substringBetween(String str) {
        if (!ObjectUtils.allNotNull(str, "calls=", ",usec")) {
            return null;
        } else {
            int start = str.indexOf("calls=");
            if (start != -1) {
                int end = str.indexOf(",usec", start + "calls=".length());
                if (end != -1) {
                    return str.substring(start + "calls=".length(), end);
                }
            }
            return null;
        }
    }
}
