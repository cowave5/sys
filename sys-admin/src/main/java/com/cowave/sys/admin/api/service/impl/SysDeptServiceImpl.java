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

import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Asserts;
import com.cowave.sys.admin.api.service.SysDeptService;
import com.cowave.sys.model.admin.SysDeptPost;
import com.cowave.sys.model.admin.SysUser;
import com.cowave.sys.model.admin.SysUserDept;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cowave.sys.admin.api.mapper.SysDeptMapper;
import com.cowave.sys.model.admin.SysDept;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class SysDeptServiceImpl implements SysDeptService {

	private final SysDeptMapper sysDeptMapper;

	@Override
	public Page<SysDept> list(SysDept sysDept) {
		return sysDeptMapper.list(Access.page(), sysDept);
	}

	@Override
	public SysDept info(Long deptId) {
		return sysDeptMapper.info(deptId);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void add(SysDept sysDept) throws Exception {
		// 新增部门
		sysDeptMapper.insert(sysDept);
		// 上级部门
		inputDeptParents(sysDept, false);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public SysDept edit(SysDept sysDept) throws Exception {
		Asserts.notNull(sysDept.getDeptId(), "{dept.notnull.id}");
		SysDept preDept = sysDeptMapper.info(sysDept.getDeptId());
		Asserts.notNull(preDept, "{dept.notexist}");
		Asserts.equals(0, preDept.getReadOnly(), "{dept.forbid.edit.readonly}");
		// 更新部门
		sysDeptMapper.update(sysDept);
		// 上级部门
		inputDeptParents(sysDept, true);
		return preDept;
	}

	private void inputDeptParents(SysDept sysDept, boolean overwrite){
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
	public List<SysDept> delete(Long[] deptId) throws Exception {
		Asserts.equals(0, sysDeptMapper.countReadOnlyByIdArray(deptId), "{dept.forbid.delete.readonly}");
		Asserts.equals(0, sysDeptMapper.countChildByIdArray(deptId), "{dept.forbid.delete.haschild}");
		List<SysDept> list = sysDeptMapper.queryByIdArray(deptId);
		// 删除部门
		sysDeptMapper.deleteByIdArray(deptId);
		// 上级部门关系
		sysDeptMapper.deleteDeptParentsByIdArray(deptId);
		// 部门用户关系
		sysDeptMapper.deleteDeptUserByIdArray(deptId);
		return list;
	}

	@Override
	public void changeReadonly(SysDept sysDept) {
		sysDeptMapper.changeReadonly(sysDept);
	}

	@Override
	public List<SysDeptPost> getPostsById(Long deptId) {
		return sysDeptMapper.getPostsById(deptId);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void setPosts(List<SysDeptPost> list) {
		int defaultCount = 0;
		for(SysDeptPost post : list){
			if(post.getIsDefault() == 1){
				defaultCount++;
			}
		}
		Asserts.isTrue(defaultCount <=1, "{dept.post.default.max}");
		sysDeptMapper.deleteDeptPosts(list.get(0).getDeptId());
		sysDeptMapper.insertDeptPosts(list);
	}

	@Override
	public List<SysUserDept> getUsersById(Long deptId) {
		return sysDeptMapper.getUsersById(deptId);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void setUsers(List<SysUserDept> list) {
		sysDeptMapper.deleteDeptUsers(list.get(0).getDeptId());
		sysDeptMapper.insertDeptUsers(list);
	}

	@Override
	public List<SysUser> getUsersByCode(String deptCode) {
		return sysDeptMapper.getUsersByCode(deptCode);
	}
}
