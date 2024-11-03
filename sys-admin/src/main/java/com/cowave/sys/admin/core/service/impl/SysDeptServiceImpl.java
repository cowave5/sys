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
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.response.Response;
import com.cowave.commons.response.exception.Asserts;
import com.cowave.sys.admin.core.SysRedisHelper;
import com.cowave.sys.admin.core.dao.SysDeptDao;
import com.cowave.sys.admin.core.dao.SysDeptTreeDao;
import com.cowave.sys.admin.core.dao.SysUserDao;
import com.cowave.sys.admin.core.dao.mapper.dto.SysDeptDtoMapper;
import com.cowave.sys.admin.core.entity.dto.*;
import com.cowave.sys.admin.core.entity.request.DeptCreate;
import com.cowave.sys.admin.core.entity.request.DeptQuery;
import com.cowave.sys.admin.core.service.SysDeptService;
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
@Transactional(rollbackFor = Exception.class)
public class SysDeptServiceImpl implements SysDeptService {
	private final SysRedisHelper sysRedisHelper;
	private final SysUserDao sysUserDao;
	private final SysDeptDao sysDeptDao;
	private final SysDeptTreeDao sysDeptTreeDao;
	private final SysDeptDtoMapper sysDeptMapper;

	@Override
	public List<SysDeptListDto> list(DeptQuery deptQuery) {
		return sysDeptMapper.list(deptQuery);
	}

	@Override
	public SysDeptInfoDto info(Integer deptId) {
		return sysDeptMapper.info(deptId);
	}

	@Override
	public void create(DeptCreate deptCreate) throws Exception {
		// 新增部门
		deptCreate.setCreateInfo(Access.accessInfo());
		sysDeptDao.save(deptCreate);
		// 上级部门
		sysDeptTreeDao.saveBatch(deptCreate.getDeptParents());
	}

	@Override
	public SysDeptInfoDto edit(DeptCreate deptCreate) throws Exception {
		Integer deptId = deptCreate.getDeptId();
		Asserts.notNull(deptId, "{dept.notnull.id}");
		// 校验
		SysDeptInfoDto preDept = sysDeptMapper.info(deptId);
		Asserts.notNull(preDept, "{dept.notexist}");
		Asserts.equals(0, preDept.getReadOnly(), "{dept.forbid.edit.readonly}");
		// 更新部门
		sysDeptDao.updateDept(deptCreate);
		// 上级部门
		sysDeptTreeDao.deleteParentsByDeptId(deptId);
		List<Integer> parentIds = deptCreate.getParentIds();
		if(CollectionUtils.isNotEmpty(parentIds)){
			// 检测循环，children与parents不能有交集
			List<Integer> childIds = sysDeptMapper.childIds(deptId);
			childIds.add(deptId);
			Asserts.isTrue(Collections.disjoint(childIds, parentIds), "{user.tree.cycle}");
			sysDeptTreeDao.saveBatch(deptCreate.getDeptParents());
		}
		return preDept;
	}

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
	public Response.Page<SysDeptUserDto> members(Integer deptId) {
		return new Response.Page<>(sysDeptMapper.members(
				deptId, Access.pageSize(), Access.pageOffset()), sysUserDao.countUser());
	}

	@Override
	public void changeReadonly(SysDeptDto sysDept) {
		sysDeptMapper.changeReadonly(sysDept);
	}

	@Override
	public List<SysDeptPostDto> getPostsById(Integer deptId) {
		return sysDeptMapper.getPostsById(deptId);
	}

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
	public List<SysUserDeptDto> getUsersById(Integer deptId) {
		return sysDeptMapper.getUsersById(deptId);
	}

	@Override
	public void setUsers(List<SysUserDeptDto> list) {
		sysDeptMapper.deleteDeptUsers(list.get(0).getDeptId());
		sysDeptMapper.insertDeptUsers(list);
	}

	@Override
	public List<SysUserDto> getUsersByCode(String deptCode) {
		return sysDeptMapper.getUsersByCode(deptCode);
	}

	@Override
	public void refreshDeptTree() {
		sysRedisHelper.refreshDeptTree();
		sysRedisHelper.refreshDeptUserTree();
		sysRedisHelper.refreshDeptPostTree();
	}

	@Override
	public List<Tree<String>> getDeptTree(String deptId) {
		return sysRedisHelper.getDeptTree(deptId);
	}
}
