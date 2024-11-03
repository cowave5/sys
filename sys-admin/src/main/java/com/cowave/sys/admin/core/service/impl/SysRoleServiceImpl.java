/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.response.exception.Asserts;
import com.cowave.sys.admin.core.service.SysRoleService;
import com.cowave.sys.admin.core.entity.vo.UserAuthed;
import com.cowave.sys.admin.core.dao.mapper.dto.SysRoleDtoMapper;
import com.cowave.sys.admin.core.entity.dto.SysRoleDto;
import com.cowave.sys.admin.core.entity.dto.SysUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleDtoMapper sysRoleMapper;

    @Override
    public Page<SysRoleDto> list(SysRoleDto sysRole) {
        return sysRoleMapper.list(Access.page(), sysRole);
    }

    @Override
    public SysRoleDto info(Long roleId) {
        return sysRoleMapper.info(roleId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(SysRoleDto sysRole) {
    	int codeCount = sysRoleMapper.countRoleCode(sysRole.getRoleCode(), null);
    	Asserts.equals(0, codeCount, "{role.conflict.code}", sysRole.getRoleCode());
    	// 新增角色
    	sysRoleMapper.insert(sysRole);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysRoleDto edit(SysRoleDto sysRole) {
    	Asserts.notNull(sysRole.getRoleId(), "{role.notnull.id}");
    	int codeCount = sysRoleMapper.countRoleCode(sysRole.getRoleCode(), sysRole.getRoleId());
    	Asserts.equals(0, codeCount, "{role.conflict.code}", sysRole.getRoleCode());

    	SysRoleDto preRole = sysRoleMapper.info(sysRole.getRoleId());
    	Asserts.notNull(preRole, "{role.notexist}", sysRole.getRoleId());
    	Asserts.notEquals(1, preRole.getReadOnly(), "{role.forbid.edit.readonly}", preRole.getRoleName());
        // 更新角色
    	sysRoleMapper.update(sysRole);
    	return preRole;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<SysRoleDto> delete(Long[] roleId) {
        Asserts.equals(0, sysRoleMapper.countReadOnlyByIdArray(roleId), "{role.forbid.delete.readonly}");
        List<SysRoleDto> list = sysRoleMapper.queryByIdArray(roleId);
        // 删除角色
    	sysRoleMapper.deleteByIdArray(roleId);
        // 角色菜单
        sysRoleMapper.deleteRoleMenusByIdArray(roleId);
        // 角色用户
        sysRoleMapper.deleteRoleUsersByIdArray(roleId);
    	return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeMenus(SysRoleDto sysRole) {
        Asserts.notNull(sysRole.getRoleId(), "{role.notnull.id}");
        SysRoleDto preRole = sysRoleMapper.info(sysRole.getRoleId());
        Asserts.notNull(preRole, "{role.notexist}", sysRole.getRoleId());
        Asserts.notEquals(1, preRole.getReadOnly(), "{role.forbid.edit.readonly}", preRole.getRoleName());

        sysRoleMapper.deleteRoleMenus(sysRole.getRoleId());
        sysRoleMapper.insertRoleMenus(sysRole.getRoleId(), sysRole.getMenuIds());
    }

    @Override
    public List<SysUserDto> userAuthed(UserAuthed userAuthed) {
        return sysRoleMapper.userAuthed(userAuthed);
    }

    @Override
    public List<SysUserDto> userUnAuthed(UserAuthed userAuthed) {
        return sysRoleMapper.userUnAuthed(userAuthed);
    }

    @Override
    public void grant(UserAuthed userAuthed) {
        Asserts.notNull(userAuthed.getUserIds(), "{role.notnull.userIds}");
        sysRoleMapper.grant(userAuthed);
    }

    @Override
    public void cancel(UserAuthed userAuthed) {
        Asserts.notNull(userAuthed.getUserIds(), "{role.notnull.userIds}");
        sysRoleMapper.cancel(userAuthed);
    }

    @Override
    public void changeReadonly(SysRoleDto sysRole) {
        sysRoleMapper.changeReadonly(sysRole);
    }
}
