/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.base.infra.dao.mapper.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.base.domain.dto.SysConfigDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface SysConfigDtoMapper {

    /**
     * 列表
     */
    Page<SysConfigDto> list(Page<SysConfigDto> page, @Param("config") SysConfigDto sysConfig);

    /**
     * 详情
     */
    SysConfigDto info(Integer configId);

    /**
     * 新增
     */
    void insert(SysConfigDto sysConfig);

    /**
     * 修改
     */
    void update(SysConfigDto sysConfig);

    /**
     * 多选
     */
    List<SysConfigDto> choose(Integer[] array);

    /**
     * 删除
     */
    void delete(Integer[] array);
}
