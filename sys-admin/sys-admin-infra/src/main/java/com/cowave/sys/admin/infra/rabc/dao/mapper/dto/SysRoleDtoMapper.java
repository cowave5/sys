/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.rabc.dao.mapper.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.domain.rabc.dto.RoleUserDto;
import com.cowave.sys.admin.domain.rabc.dto.SysRoleDto;
import com.cowave.sys.admin.domain.rabc.vo.UserAuthed;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色
 *
 * @author shanhuiming
 */
@Mapper
public interface SysRoleDtoMapper {

    /**
     * 列表
     */
    Page<SysRoleDto> list(Page<SysRoleDto> page, @Param("role") SysRoleDto sysRole);

    /**
     * 新增
     */
    void insert(SysRoleDto sysRole);

    /**
     * 详情
     */
    SysRoleDto info(Integer roleId);

    /**
     * 用户角色code
     */
    List<String> getRoleCodesByUserId(Integer userId);

    /**
     * 角色名称计数
     */
    int countRoleName(@Param("roleName") String roleName, @Param("roleId") Integer roleId);

    /**
     * 角色编码计数
     */
    int countRoleCode(@Param("roleCode") String roleCode, @Param("roleId") Integer roleId);

    /**
     * 更新
     */
    void update(SysRoleDto sysRole);

    /**
     * 删除
     */
    void delete(Integer roleId);

    /**
     * 删除角色用户
     */
    void deleteUsers(Integer roleId);

    /**
     * 已授权用户
     */
    Page<RoleUserDto> userAuthed(Page<RoleUserDto> page, @Param("user") UserAuthed userAuthed);

    /**
     * 未授权用户
     */
    Page<RoleUserDto> userUnAuthed(Page<RoleUserDto> page, @Param("user") UserAuthed userAuthed);

    /**
     * 授权用户
     */
    void grant(UserAuthed userAuthed);

    /**
     * 取消授权
     */
    void cancel(UserAuthed userAuthed);

    /**
     * 只读计数
     */
    int countReadOnlyByIdArray(Long[] array);

    /**
     * id列表查询
     */
    List<SysRoleDto> queryByIdArray(Long[] array);

    /**
     * 删除
     */
    int deleteByIdArray(Long[] array);

    /**
     * 修改只读
     */
    void changeReadonly(SysRoleDto sysRole);

    /**
     * 删除角色菜单
     */
    int deleteRoleMenusByIdArray(Long[] array);

    /**
     * 删除角色用户
     */
    int deleteRoleUsersByIdArray(Long[] array);

    /**
     * 删除角色菜单
     */
    void deleteRoleMenus(Integer roleId);

    /**
     * 插入角色菜单
     */
    void insertRoleMenus(@Param("roleId") Integer roleId, @Param("list") List<Long> menuIds);

    /**
     * 获取角色code
     */
    String queryCodeById(Integer id);
}
