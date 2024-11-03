/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.cache;

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.commons.framework.helper.redis.dict.DictValueParser;
import com.cowave.sys.admin.core.mapper.SysConfigMapper;
import com.cowave.sys.model.admin.SysConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author shanhuiming
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysConfigCache implements ApplicationRunner {

    public static final String KEY_CONFIG = "sys:config:";

    private final Map<String, DictValueParser> parserMap = new ConcurrentHashMap<>();

    private final RedisHelper redisHelper;

    private final SysConfigMapper sysConfigMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        refresh();
    }

    public void refresh() throws Exception {
        log.info("refresh cache of config ...");
        Collection<String> keys = redisHelper.keys(KEY_CONFIG + "*");
        redisHelper.delete(keys);
        List<SysConfig> list = sysConfigMapper.list(Access.page(Integer.MAX_VALUE), new SysConfig()).getRecords();
        for(SysConfig conf : list) {
            putValue(conf);
        }
    }

    public <T> T getValue(String configKey) {
        return redisHelper.getValue(KEY_CONFIG + configKey);
    }

    public void putValue(SysConfig conf) throws Exception {
        String parserClazz = conf.getValueParser();
        if(StringUtils.isBlank(parserClazz)){
            redisHelper.putValue(KEY_CONFIG + conf.getConfigKey(), conf.getConfigValue());
        }else{
            DictValueParser parser = parserMap.get(parserClazz);
            if(parser == null){
                parser = (DictValueParser)Class.forName(parserClazz).getDeclaredConstructor().newInstance();
                parserMap.put(parserClazz, parser);
            }
            redisHelper.putValue(KEY_CONFIG + conf.getConfigKey(), parser.parse(conf.getConfigValue(), conf.getValueParam()));
        }
    }

    public void delete(SysConfig conf) {
        redisHelper.delete(KEY_CONFIG + conf.getConfigKey());
    }
}
