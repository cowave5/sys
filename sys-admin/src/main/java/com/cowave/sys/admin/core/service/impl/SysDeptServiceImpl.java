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

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.response.Response;
import com.cowave.commons.response.exception.Asserts;
import com.cowave.sys.admin.core.SysRedisHelper;
import com.cowave.sys.admin.core.dao.SysUserDao;
import com.cowave.sys.admin.core.entity.dto.*;
import com.cowave.sys.admin.core.service.SysDeptService;
import com.cowave.sys.admin.core.dao.mapper.dto.SysDeptDtoMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class SysDeptServiceImpl implements SysDeptService {
	private final SysRedisHelper sysRedisHelper;
	private final SysUserDao sysUserDao;
	private final SysDeptDtoMapper sysDeptMapper;

	@Override
	public Page<SysDeptDto> list(SysDeptDto sysDept) {
		return sysDeptMapper.list(Access.page(), sysDept);
	}

	@Override
	public SysDeptDto info(Long deptId) {
		return sysDeptMapper.info(deptId);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void add(SysDeptDto sysDept) throws Exception {
		// 新增部门
		sysDeptMapper.insert(sysDept);
		// 上级部门
		inputDeptParents(sysDept, false);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public SysDeptDto edit(SysDeptDto sysDept) throws Exception {
		Asserts.notNull(sysDept.getDeptId(), "{dept.notnull.id}");
		SysDeptDto preDept = sysDeptMapper.info(sysDept.getDeptId());
		Asserts.notNull(preDept, "{dept.notexist}");
		Asserts.equals(0, preDept.getReadOnly(), "{dept.forbid.edit.readonly}");
		// 更新部门
		sysDeptMapper.update(sysDept);
		// 上级部门
		inputDeptParents(sysDept, true);
		return preDept;
	}

	private void inputDeptParents(SysDeptDto sysDept, boolean overwrite){
		if(overwrite){
			sysDeptMapper.deleteDeptParents(sysDept.getDeptId());
		}
		List<Long> parentIds = sysDept.getParentIds();
		if(CollectionUtils.isNotEmpty(parentIds)) {
			if(overwrite){
				List<Long> childIds = sysDeptMapper.childIds(sysDept.getDeptId());
				childIds.add(sysDept.getDeptId());
				Asserts.isTrue(Collections.disjoint(childIds, parentIds), "{user.tree.cycle}");
			}
			sysDeptMapper.insertDeptParents(sysDept.getDeptId(), parentIds);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public List<SysDeptDto> delete(Long[] deptId) throws Exception {
		Asserts.equals(0, sysDeptMapper.countReadOnlyByIdArray(deptId), "{dept.forbid.delete.readonly}");
		Asserts.equals(0, sysDeptMapper.countChildByIdArray(deptId), "{dept.forbid.delete.haschild}");
		List<SysDeptDto> list = sysDeptMapper.queryByIdArray(deptId);
		// 删除部门
		sysDeptMapper.deleteByIdArray(deptId);
		// 上级部门关系
		sysDeptMapper.deleteDeptParentsByIdArray(deptId);
		// 部门用户关系
		sysDeptMapper.deleteDeptUserByIdArray(deptId);
		return list;
	}

	@Override
	public Response.Page<SysDeptUserDto> members(long deptId) {
		return new Response.Page<>(sysDeptMapper.members(
				deptId, Access.pageSize(), Access.pageOffset()), sysUserDao.countUser());
	}

	@Override
	public void changeReadonly(SysDeptDto sysDept) {
		sysDeptMapper.changeReadonly(sysDept);
	}

	@Override
	public List<SysDeptPostDto> getPostsById(Long deptId) {
		return sysDeptMapper.getPostsById(deptId);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void setPosts(List<SysDeptPostDto> list) {
		int defaultCount = 0;
		for(SysDeptPostDto post : list){
			if(post.getIsDefault() == 1){
				defaultCount++;
			}
		}
		Asserts.isTrue(defaultCount <=1, "{dept.post.default.max}");
		sysDeptMapper.deleteDeptPosts(list.get(0).getDeptId());
		sysDeptMapper.insertDeptPosts(list);
	}

	@Override
	public List<SysUserDeptDto> getUsersById(Long deptId) {
		return sysDeptMapper.getUsersById(deptId);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void setUsers(List<SysUserDeptDto> list) {
		sysDeptMapper.deleteDeptUsers(list.get(0).getDeptId());
		sysDeptMapper.insertDeptUsers(list);
	}

	@Override
	public List<SysUserDto> getUsersByCode(String deptCode) {
		return sysDeptMapper.getUsersByCode(deptCode);
	}

	/**
	 * 刷新缓存
	 */
	@Override
	public void refreshDeptTree() {
		sysRedisHelper.refreshDeptTree();
		sysRedisHelper.refreshDeptUserTree();
		sysRedisHelper.refreshDeptPostTree();
	}

	/**
	 * 部门树
	 */
	@Override
	public List<Tree<String>> getDeptTree(String deptId) {
		return sysRedisHelper.getDeptTree(deptId);
	}
}
