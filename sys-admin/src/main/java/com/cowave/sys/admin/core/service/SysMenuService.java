/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.service;

import java.util.List;

import com.cowave.sys.admin.core.entity.vo.RoleAuthed;
import com.cowave.sys.admin.core.entity.dto.SysMenuDto;
import com.cowave.sys.admin.core.entity.dto.SysRoleDto;

import cn.hutool.core.lang.tree.Tree;

/**
 * @author shanhuiming
 */
public interface SysMenuService{

	/**
	 * 树
	 */
	List<Tree<Long>> tree();

	/**
	 * 列表
	 */
	List<SysMenuDto> list(String menuName, Integer menuStatus, Integer visible, boolean filterRole);

	/**
	 * OAuth菜单
	 */
	List<SysMenuDto> oauthMenus(Long userId);

	/**
	 * 详情
	 */
	SysMenuDto info(Long menuId);

	/**
	 * 新增
	 */
	void add(SysMenuDto sysMenu);

	/**
	 * 修改
	 */
	void edit(SysMenuDto sysMenu);

	/**
	 * 删除
	 */
	void delete(Long menuId);

	/**
	 * 修改只读
	 */
	void changeReadonly(SysMenuDto sysMenu);

	/**
     * 已授权角色
     */
    List<SysRoleDto> roleAuthed(RoleAuthed roleAuthed);

    /**
     * 未授权角色
     */
    List<SysRoleDto> roleUnAuthed(RoleAuthed roleAuthed);

    /**
     * 授权角色
     */
    void grant(RoleAuthed roleAuthed);

    /**
     * 取消授权
     */
    void cancel(RoleAuthed roleAuthed);
}
