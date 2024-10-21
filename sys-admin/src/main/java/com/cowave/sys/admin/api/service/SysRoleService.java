/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.api.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.core.entity.UserAuthed;
import com.cowave.sys.model.admin.SysRole;
import com.cowave.sys.model.admin.SysUser;

/**
 *
 * @author shanhuiming
 *
 */
public interface SysRoleService {

    /**
     * 列表
     */
    Page<SysRole> list(SysRole role);

    /**
     * 详情
     */
    SysRole info(Long roleId);

    /**
     * 新增
     */
    void add(SysRole sysRole);

    /**
     * 修改
     */
    SysRole edit(SysRole sysRole);

    /**
     * 删除
     */
    List<SysRole> delete(Long[] roleId);

    /**
     * 修改角色菜单
     */
    void changeMenus(SysRole sysRole);

    /**
     * 已授权用户
     */
    List<SysUser> userAuthed(UserAuthed userAuthed);

    /**
     * 未授权用户
     */
    List<SysUser> userUnAuthed(UserAuthed userAuthed);

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
    void changeReadonly(SysRole sysRole);
}
