/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.response.exception.Asserts;
import com.cowave.sys.admin.core.SysRedisHelper;
import com.cowave.sys.admin.core.dao.mapper.dto.SysConfigDtoMapper;
import com.cowave.sys.admin.core.entity.dto.SysConfigDto;
import com.cowave.sys.admin.core.service.SysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class SysConfigServiceImpl implements SysConfigService {

    private final SysRedisHelper sysRedisHelper;

    private final SysConfigDtoMapper sysConfigMapper;

    @Override
    public Page<SysConfigDto> list(SysConfigDto sysConfig) {
        return sysConfigMapper.list(Access.page(), sysConfig);
    }

    @Override
    public SysConfigDto info(Integer configId) {
        return sysConfigMapper.info(configId);
    }

    @Override
    public void add(SysConfigDto sysConfig) throws Exception {
        sysConfigMapper.insert(sysConfig);
        sysRedisHelper.putConfig(sysConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(SysConfigDto sysConfig) throws Exception {
        Asserts.notNull(sysConfig.getConfigId(), "{config.notnull.id}");
        SysConfigDto preConfig = sysConfigMapper.info(sysConfig.getConfigId());
        Asserts.notNull(preConfig, "{config.notexist}", sysConfig.getConfigId());
        // 校验parser
        sysConfigMapper.update(sysConfig);
        sysRedisHelper.putConfig(sysConfig);
    }

    @Override
    public void delete(Integer[] configId) throws Exception {
        List<SysConfigDto> list = sysConfigMapper.choose(configId);
        sysConfigMapper.delete(configId);
        for(SysConfigDto conf : list) {
            sysRedisHelper.removeConfig(conf);
        }
    }

    /**
     * 刷新缓存
     */
    @Override
    public void refresh() throws Exception {
        sysRedisHelper.refreshConfig();
    }

    /**
     * 获取配置
     */
    @Override
    public String getValue(String configKey) {
        return sysRedisHelper.getConfig(configKey);
    }
}
