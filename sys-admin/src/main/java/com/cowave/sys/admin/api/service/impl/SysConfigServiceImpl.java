/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.api.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Asserts;
import com.cowave.sys.admin.core.caches.SysConfigCaches;
import com.cowave.sys.admin.core.mapper.SysConfigMapper;
import com.cowave.sys.admin.api.service.SysConfigService;
import com.cowave.sys.model.admin.SysConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class SysConfigServiceImpl implements SysConfigService {

    private final SysConfigCaches sysConfigCaches;

    private final SysConfigMapper sysConfigMapper;

    @Override
    public Page<SysConfig> list(SysConfig sysConfig) {
        return sysConfigMapper.list(Access.page(), sysConfig);
    }

    @Override
    public SysConfig info(Integer configId) {
        return sysConfigMapper.info(configId);
    }

    @Override
    public void add(SysConfig sysConfig) throws Exception {
        sysConfigMapper.insert(sysConfig);
        sysConfigCaches.putValue(sysConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(SysConfig sysConfig) throws Exception {
        // 根据id修改，configKey可以改变
        Asserts.notNull(sysConfig.getConfigId(), "{config.notnull.id}");
        SysConfig preConfig = sysConfigMapper.info(sysConfig.getConfigId());
        Asserts.notNull(preConfig, "{config.notexist}", sysConfig.getConfigId());
        // 校验parser
        sysConfigMapper.update(sysConfig);
        sysConfigCaches.putValue(sysConfig);
    }

    @Override
    public void delete(Integer[] configId) throws Exception {
        List<SysConfig> list = sysConfigMapper.choose(configId);
        sysConfigMapper.delete(configId);
        for(SysConfig conf : list) {
            sysConfigCaches.delete(conf);
        }
    }
}
