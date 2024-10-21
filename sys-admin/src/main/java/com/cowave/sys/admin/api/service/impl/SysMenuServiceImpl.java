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

import java.util.List;

import com.cowave.commons.tools.Asserts;
import com.cowave.sys.admin.api.service.SysMenuService;
import org.springframework.stereotype.Service;

import com.cowave.sys.admin.core.entity.RoleAuthed;
import com.cowave.sys.admin.core.mapper.SysMenuMapper;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.model.admin.SysMenu;
import com.cowave.sys.model.admin.SysRole;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import lombok.RequiredArgsConstructor;

/**
 * 菜单
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class SysMenuServiceImpl implements SysMenuService {

	private final TreeNodeConfig treeConfig = new TreeNodeConfig()
			.setIdKey("id").setParentIdKey("pid").setNameKey("label").setChildrenKey("children");

	private final SysMenuMapper sysMenuMapper;

	@Override
	public List<Tree<Long>> tree() {
		List<SysMenu>list = list(null, 1, null,false);
		return TreeUtil.build(list, 0L, treeConfig, (menu, node) -> {
			node.setId(menu.getMenuId());
			node.setParentId(menu.getParentId());
			node.setName(menu.getMenuName());
		});
	}

	@Override
	public List<SysMenu> list(String menuName, Integer menuStatus, Integer visible, boolean filterRole) {
		if(!filterRole || Access.userIsAdmin()){
			return sysMenuMapper.list(visible, menuName, menuStatus);
		}
		return sysMenuMapper.userMenus(Access.userId(), visible, menuName, menuStatus);
	}

	@Override
	public List<SysMenu> oauthMenus(Long userId) {
		return sysMenuMapper.oauthMenus(userId);
	}

	@Override
	public SysMenu info(Long menuId) {
		return sysMenuMapper.info(menuId);
	}

	@Override
	public void add(SysMenu sysMenu) {
		sysMenuMapper.insert(sysMenu);
	}

	@Override
	public void edit(SysMenu sysMenu) {
		Asserts.notNull(sysMenu.getMenuId(), "{menu.notnull.id}");
		SysMenu preMenu = sysMenuMapper.info(sysMenu.getMenuId());
		Asserts.notNull(preMenu, "{menu.notexist}", sysMenu.getMenuId());
		Asserts.notEquals(1, preMenu.getReadOnly(), "{menu.forbid.edit.readonly}");
		if(!"C".equals(sysMenu.getMenuType())){
			sysMenu.setComponent(null);
		}
		sysMenuMapper.update(sysMenu);
	}

	@Override
	public void delete(Long menuId) {
		SysMenu preMenu = sysMenuMapper.info(menuId);
		if(preMenu == null) {
			return;
		}
		Asserts.notEquals(1, preMenu.getReadOnly(), "{menu.forbid.delete.readonly}");
		// 删除自己以及子菜单的角色
		sysMenuMapper.deleteMenus(menuId);
		// 删除自己以及子菜单
		sysMenuMapper.deletes(menuId);
	}

	@Override
	public void changeReadonly(SysMenu sysMenu) {
		sysMenuMapper.changeReadonly(sysMenu);
	}

	@Override
    public List<SysRole> roleAuthed(RoleAuthed roleAuthed) {
        return sysMenuMapper.roleAuthed(roleAuthed);
    }

    @Override
    public List<SysRole> roleUnAuthed(RoleAuthed roleAuthed) {
        return sysMenuMapper.roleUnAuthed(roleAuthed);
    }

    @Override
    public void grant(RoleAuthed roleAuthed) {
        Asserts.notNull(roleAuthed.getRoleIds(), "{menu.notnull.roleIds}");
        sysMenuMapper.grant(roleAuthed);
    }

    @Override
    public void cancel(RoleAuthed roleAuthed) {
        Asserts.notNull(roleAuthed.getRoleIds(), "{menu.notnull.roleIds}");
        sysMenuMapper.cancel(roleAuthed);
    }
}
