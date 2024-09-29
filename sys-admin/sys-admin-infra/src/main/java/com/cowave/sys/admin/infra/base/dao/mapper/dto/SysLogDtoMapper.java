/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.base.dao.mapper.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.domain.base.dto.SysLogDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface SysLogDtoMapper {

    /**
     * 列表
     */
    Page<SysLogDto> list(Page<SysLogDto> page, @Param("log") SysLogDto sysLog);

    /**
     * 新增
     */
    void insert(SysLogDto sysLog);

    /**
     * 详情
     */
    SysLogDto info(long id);

    /**
     * 删除
     */
    void delete(Long[] array);

    /**
     * 清空
     */
    void clean();

    /**
     * 查询角色名称
     */
    List<String> queryNameByRoleIds(List<Long> list);

    /**
     * 查询用户名称
     */
    List<String> queryNameByUserIds(List<Long> list);

    /**
     * 查询部门名称
     */
    List<String> queryNameByDeptIds(List<Long> list);

    /**
     * 查询岗位名称
     */
    String queryNameByPostId(Integer postId);
}
