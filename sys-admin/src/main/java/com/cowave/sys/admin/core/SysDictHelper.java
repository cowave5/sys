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

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.helper.redis.dict.DictHelper;
import com.cowave.sys.admin.core.dao.mapper.dto.SysDictDtoMapper;
import com.cowave.sys.admin.core.entity.dto.SysDictDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shanhuiming
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysDictHelper implements ApplicationRunner {

    private final DictHelper dictHelper;

    private final SysDictDtoMapper sysDictMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("init cache of dict ...");
        refresh();
    }

    public void refresh() {
        dictHelper.clear();
        List<SysDictDto> list = sysDictMapper.list(
                Access.page(Integer.MAX_VALUE), null, null).getRecords();
        for (SysDictDto sysDict : list) {
            dictHelper.put(sysDict);
        }
    }

    public void put(SysDictDto sysDict) {
        if (sysDict.getStatus() == 1) {
            dictHelper.put(sysDict);
        }
    }

    public SysDictDto getDict(String dictCode) {
        return dictHelper.getDict(dictCode);
    }

    public List<SysDictDto> getType(String typeCode) {
        return dictHelper.getType(typeCode);
    }

    public List<SysDictDto> getGroup(String groupCode) {
        return dictHelper.getGroup(groupCode);
    }

    public void removeDict(String dictCode) {
        dictHelper.removeDict(dictCode);
    }

    public void removeType(String typeCode) {
        dictHelper.removeType(typeCode);
    }

    public void removeGroup(String groupCode) {
        dictHelper.removeGroup(groupCode);
    }
}
