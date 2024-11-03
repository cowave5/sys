/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.rabc.service.impl;

import cn.hutool.core.lang.tree.Tree;
import com.cowave.commons.client.http.asserts.Asserts;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationContext;
import com.cowave.sys.admin.rabc.infra.dao.SysUserDao;
import com.cowave.sys.admin.rabc.infra.dao.SysUserDeptDao;
import com.cowave.sys.admin.rabc.infra.dao.SysUserRoleDao;
import com.cowave.sys.admin.rabc.infra.dao.SysUserTreeDao;
import com.cowave.sys.admin.rabc.infra.dao.mapper.dto.SysUserDtoMapper;
import com.cowave.sys.admin.rabc.domain.SysUser;
import com.cowave.sys.admin.rabc.domain.dto.SysUserInfoDto;
import com.cowave.sys.admin.rabc.domain.dto.SysUserListDto;
import com.cowave.sys.admin.rabc.domain.dto.SysUserNameDto;
import com.cowave.sys.admin.base.infra.redis.SysConfigRedis;
import com.cowave.sys.admin.rabc.infra.redis.SysDeptRedis;
import com.cowave.sys.admin.rabc.infra.redis.SysUserRedis;
import com.cowave.sys.admin.rabc.service.SysUserService;
import com.cowave.sys.admin.rabc.domain.request.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl implements SysUserService {
	private final PasswordEncoder passwordEncoder;

	private final SysDeptRedis sysDeptRedis;
	private final SysUserRedis sysUserRedis;
	private final SysConfigRedis sysConfigRedis;

	private final SysUserDao sysUserDao;
	private final SysUserRoleDao sysUserRoleDao;
	private final SysUserDeptDao sysUserDeptDao;
	private final SysUserTreeDao sysUserTreeDao;

	private final SysUserDtoMapper sysUserMapper;

	@Override
    public Response.Page<SysUserListDto> list(SysUserQuery query) {
		if(query.getDeptId() == null){
			return new Response.Page<>(sysUserMapper.list(query), sysUserMapper.count(query));
		}else{
			return new Response.Page<>(sysUserMapper.listOfDept(query), sysUserMapper.countOfDept(query));
		}
    }

	@Override
    public SysUserInfoDto info(Integer userId) {
        return sysUserMapper.getById(userId);
    }

    @Override
    public void create(SysUserCreate user) {
		String userAccount = user.getUserAccount();
		String userPasswd = user.getUserPasswd();
    	Asserts.notNull(userPasswd, "{user.notnull.passwd}");
		Asserts.isFalse(sysUserDao.isAccountExist(userAccount), "{user.conflict.account}", userAccount);
		// 创建用户
    	user.setUserPasswd(passwordEncoder.encode(userPasswd));
		user.setCreateInfo(Access.accessInfo());
		sysUserDao.save(user);
    	// 用户角色
		sysUserRoleDao.saveBatch(user.getUserRoles());
		// 上级用户
		sysUserTreeDao.saveBatch(user.getUserParents());
    	// 部门岗位
		sysUserDeptDao.saveBatch(user.getUserDeptPosts());
    }

    @Override
    public void edit(SysUserCreate user) {
		Integer userId = user.getUserId();
		Asserts.notNull(userId, "{user.notnull.id}");
		// 账号校验
		String userAccount = user.getUserAccount();
		Asserts.isFalse(sysUserDao.isAccountExist(userAccount, userId), "{user.conflict.account}", userAccount);
		// 操作日志
		SysUserInfoDto preUser = getAndCheckEditable(userId);
		OperationContext.prepareContent(preUser);
		// 更新用户
		sysUserDao.updateUser(user);
    	// 用户角色
		sysUserRoleDao.deleteByUserId(userId);
		sysUserRoleDao.saveBatch(user.getUserRoles());
		// 上级用户
		sysUserTreeDao.deleteParentsByUserId(userId);
		List<Integer> parentIds = user.getParentIds();
		if(CollectionUtils.isNotEmpty(parentIds)){
			// 检测循环，children与parents不能有交集
			List<Integer> childIds = sysUserMapper.childIds(userId);
			childIds.add(userId);
			Asserts.isTrue(java.util.Collections.disjoint(childIds, parentIds), "{user.tree.cycle}");
			sysUserTreeDao.saveBatch(user.getUserParents());
		}
    	// 部门岗位
		sysUserDeptDao.deleteByUserId(userId);
		sysUserDeptDao.saveBatch(user.getUserDeptPosts());
    }

	private SysUserInfoDto getAndCheckEditable(Integer userId){
		SysUserInfoDto preUser = sysUserMapper.getById(userId);
		Asserts.notNull(preUser, "{user.notexist}", userId);
		Asserts.notEquals(1, preUser.getReadOnly(), "{user.forbid.edit.readonly}", preUser.getUserAccount());
		return preUser;
	}

    @Override
    public void delete(List<Integer> userIds) {
		List<SysUserInfoDto> list = new ArrayList<>();
		for(Integer userId : userIds){
			SysUserInfoDto deleteUser = delete(userId);
			if(deleteUser != null){
				list.add(deleteUser);
			}
		}
		OperationContext.prepareContent(list);
    }

	private SysUserInfoDto delete(Integer userId){
		SysUserInfoDto preUser = sysUserMapper.getById(userId);
		if(preUser == null) {
			return null;
		}
		Asserts.notEquals(Access.userAccount(), preUser.getUserAccount(), "{user.forbid.delete.self}");
		Asserts.notEquals(1, preUser.getReadOnly(), "{user.forbid.delete.readonly}");
		// 删除用户
		sysUserDao.removeById(userId);
		// 用户角色
		sysUserRoleDao.deleteByUserId(userId);
		// 用户单位
		sysUserDeptDao.deleteByUserId(userId);
		// 上级用户
		sysUserTreeDao.deleteParentsByUserId(userId);
		// 下级用户
		sysUserTreeDao.deleteChildrenByUserId(userId);
		return preUser;
	}

	@Override
	public void changeRoles(SysUserRoleUpdate roleUpdate) {
		getAndCheckEditable(roleUpdate.getUserId());
		sysUserRoleDao.deleteByUserId(roleUpdate.getUserId());
		sysUserRoleDao.saveBatch(roleUpdate.getUserRoles());
	}

	@Override
	public void changeStatus(SysUserStatusUpdate statusUpdate) {
		getAndCheckEditable(statusUpdate.getUserId());
		sysUserDao.updateUserStatus(statusUpdate.getUserId(), statusUpdate.getUserStatus());
	}

	@Override
	public void changePasswd(SysUserPasswdUpdate passwdUpdate) {
		getAndCheckEditable(passwdUpdate.getUserId());
		String newPasswd = passwordEncoder.encode(passwdUpdate.getUserPasswd());
		sysUserDao.updateUserPasswd(passwdUpdate.getUserId(), newPasswd);
	}

	@Override
	public void changeReadonly(SysUserReadUpdate readUpdate) {
		sysUserDao.updateUserReadonly(readUpdate.getUserId(), readUpdate.getReadOnly());
	}

	@Override
	public List<SysUser> listForExport(SysUserExport userExport) {
		return sysUserDao.listForExport(userExport);
	}

	@Override
	public void refreshUserTree() {
		sysUserRedis.refreshUserTree();
		sysDeptRedis.refreshDeptUserTree();
	}

	@Override
	public Tree<String> getUserTree() {
		return sysUserRedis.getUserTree();
	}

	@Override
	public Tree<String> getDeptUserTree() {
		return sysDeptRedis.getDeptUserTree();
	}

	@Override
	public void importExcelData(List<SysUser> list, boolean overwrite) {
		String passwd = sysConfigRedis.getConfig("sys.user.initPassword");
		for(SysUser sysUser : list){
			sysUser.setUserPasswd(passwordEncoder.encode(passwd));
			sysUser.setCreateUser(Access.userId());
			sysUser.setCreateDept(Access.deptId());
			sysUser.setCreateTime(Access.accessTime());
		}
		list.forEach(sysUser -> sysUser.setUserPasswd(passwordEncoder.encode(passwd)));
		sysUserMapper.batchInsert(list, overwrite);
	}

	@Override
	public List<SysUserNameDto> leaders(Integer userId) {
		if(userId == null){
			userId = Access.userId();
		}
		return sysUserMapper.leaders(userId);
	}
}
