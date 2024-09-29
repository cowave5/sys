/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.rabc.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.cowave.commons.client.http.asserts.Asserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.domain.rabc.SysMenu;
import com.cowave.sys.admin.domain.rabc.request.MenuReadUpdate;
import com.cowave.sys.admin.infra.rabc.dao.SysMenuDao;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysMenuDtoMapper;
import com.cowave.sys.admin.service.rabc.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜单
 *
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysMenuServiceImpl implements SysMenuService {
	private final TreeNodeConfig treeConfig = new TreeNodeConfig()
			.setIdKey("id").setParentIdKey("pid").setNameKey("label").setChildrenKey("children");
	private final SysMenuDao sysMenuDao;
	private final SysMenuDtoMapper sysMenuDtoMapper;

	@Override
	public List<Tree<Integer>> tree() {
		List<SysMenu> list = list(null, 1, null,false);
		return TreeUtil.build(list, 0, treeConfig, (menu, node) -> {
			node.setId(menu.getMenuId());
			node.setParentId(menu.getParentId());
			node.setName(menu.getMenuName());
		});
	}

	@Override
	public List<SysMenu> list(String menuName, Integer menuStatus, Integer visible, boolean filterRole) {
		if(!filterRole || Access.userIsAdmin()){
			return sysMenuDao.queryList(visible, menuStatus, menuName);
		}
		return sysMenuDtoMapper.userMenus(Access.userId(), visible, menuName, menuStatus);
	}

	@Override
	public SysMenu info(Integer menuId) {
		return sysMenuDao.getById(menuId);
	}

	@Override
	public void add(SysMenu sysMenu) {
		sysMenu.setCreateInfo(Access.accessInfo());
		sysMenuDao.save(sysMenu);
	}

	@Override
	public void delete(Integer menuId) {
		SysMenu preMenu = sysMenuDao.getById(menuId);
		if(preMenu == null) {
			return;
		}
		Asserts.notEquals(1, preMenu.getReadOnly(), "{menu.forbid.delete.readonly}");
		// 删除当前以及子菜单的角色关联
		sysMenuDtoMapper.loopDeleteMenuRoles(menuId);
		// 删除自己以及子菜单
		sysMenuDtoMapper.loopDeleteMenus(menuId);
	}

	@Override
	public void edit(SysMenu sysMenu) {
		Asserts.notNull(sysMenu.getMenuId(), "{menu.notnull.id}");

		SysMenu preMenu = sysMenuDao.getById(sysMenu.getMenuId());
		Asserts.notNull(preMenu, "{menu.notexist}", sysMenu.getMenuId());
		Asserts.notEquals(1, preMenu.getReadOnly(), "{menu.forbid.edit.readonly}");

		if(!"C".equals(sysMenu.getMenuType())){
			sysMenu.setComponent(null);
		}
		sysMenuDao.updateMenu(sysMenu);
	}

	@Override
	public void updateReadonly(MenuReadUpdate menuUpdate) {
		sysMenuDao.updateReadonly(menuUpdate.getMenuId(), menuUpdate.getReadOnly());
	}

	@Override
	public List<SysMenu> oauthMenus(Integer userId) {
		return sysMenuDtoMapper.oauthMenus(userId);
	}
}
