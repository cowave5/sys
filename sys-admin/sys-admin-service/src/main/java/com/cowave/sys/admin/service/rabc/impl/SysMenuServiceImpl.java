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
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysMenuDtoMapper;
import com.cowave.sys.admin.domain.rabc.dto.SysMenuDto;
import com.cowave.sys.admin.domain.rabc.dto.SysRoleDto;
import com.cowave.sys.admin.domain.rabc.vo.RoleAuthed;
import com.cowave.sys.admin.service.rabc.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

	private final SysMenuDtoMapper sysMenuMapper;

	@Override
	public List<Tree<Integer>> tree() {
		List<SysMenuDto> list = list(null, 1, null,false);
		return TreeUtil.build(list, 0, treeConfig, (menu, node) -> {
			node.setId(menu.getMenuId());
			node.setParentId(menu.getParentId());
			node.setName(menu.getMenuName());
		});
	}

	@Override
	public List<SysMenuDto> list(String menuName, Integer menuStatus, Integer visible, boolean filterRole) {
		if(!filterRole || Access.userIsAdmin()){
			return sysMenuMapper.list(visible, menuName, menuStatus);
		}
		return sysMenuMapper.userMenus(Access.userId(), visible, menuName, menuStatus);
	}

	@Override
	public List<SysMenuDto> oauthMenus(Integer userId) {
		return sysMenuMapper.oauthMenus(userId);
	}

	@Override
	public SysMenuDto info(Integer menuId) {
		return sysMenuMapper.info(menuId);
	}

	@Override
	public void add(SysMenuDto sysMenu) {
		sysMenuMapper.insert(sysMenu);
	}

	@Override
	public void edit(SysMenuDto sysMenu) {
		Asserts.notNull(sysMenu.getMenuId(), "{menu.notnull.id}");
		SysMenuDto preMenu = sysMenuMapper.info(sysMenu.getMenuId());
		Asserts.notNull(preMenu, "{menu.notexist}", sysMenu.getMenuId());
		Asserts.notEquals(1, preMenu.getReadOnly(), "{menu.forbid.edit.readonly}");
		if(!"C".equals(sysMenu.getMenuType())){
			sysMenu.setComponent(null);
		}
		sysMenuMapper.update(sysMenu);
	}

	@Override
	public void delete(Integer menuId) {
		SysMenuDto preMenu = sysMenuMapper.info(menuId);
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
	public void changeReadonly(SysMenuDto sysMenu) {
		sysMenuMapper.changeReadonly(sysMenu);
	}

	@Override
    public List<SysRoleDto> roleAuthed(RoleAuthed roleAuthed) {
        return sysMenuMapper.roleAuthed(roleAuthed);
    }

    @Override
    public List<SysRoleDto> roleUnAuthed(RoleAuthed roleAuthed) {
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
