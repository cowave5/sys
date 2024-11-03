/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.rabc;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.domain.rabc.dto.RoleUserDto;
import com.cowave.sys.admin.domain.rabc.dto.SysRoleDto;
import com.cowave.sys.admin.domain.rabc.vo.UserAuthed;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysRoleService {

    /**
     * 列表
     */
    Page<SysRoleDto> list(SysRoleDto role);

    /**
     * 详情
     */
    SysRoleDto info(Integer roleId);

    /**
     * 新增
     */
    void add(SysRoleDto sysRole);

    /**
     * 修改
     */
    SysRoleDto edit(SysRoleDto sysRole);

    /**
     * 删除
     */
    List<SysRoleDto> delete(Long[] roleId);

    /**
     * 修改角色菜单
     */
    void changeMenus(SysRoleDto sysRole);

    /**
     * 已授权用户
     */
    Page<RoleUserDto> userAuthed(UserAuthed userAuthed);

    /**
     * 未授权用户
     */
    Page<RoleUserDto> userUnAuthed(UserAuthed userAuthed);

    /**
     * 授权用户
     */
    void grant(UserAuthed userAuthed);

    /**
     * 取消授权
     */
    void cancel(UserAuthed userAuthed);

    /**
     * 修改只读
     */
    void changeReadonly(SysRoleDto sysRole);
}
