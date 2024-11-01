/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.core.entity.TreeNode;
import com.cowave.sys.model.admin.SysDeptPost;
import com.cowave.sys.model.admin.SysUser;
import com.cowave.sys.model.admin.SysUserDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cowave.sys.model.admin.SysDept;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface SysDeptMapper {

	/**
	 * 树
	 */
	List<TreeNode> listTree();

	/**
	 * 列表
	 */
	Page<SysDept> list(Page<SysDept> page, @Param("dept") SysDept sysDept);

	/**
	 * 详情
	 */
	SysDept info(long deptId);

	/**
	 * 新增
	 */
	void insert(SysDept sysDept);

	/**
	 * 更新
	 */
	void update(SysDept sysDept);

	/**
	 * 删除上级部门
	 */
	void deleteDeptParents(long deptId);

	/**
	 * 下级部门id列表
	 */
	List<Long> childIds(long deptId);

	/**
	 * 插入上级部门
	 */
	void insertDeptParents(@Param("deptId") long deptId, @Param("list") List<Long> list);

	/**
	 * 下级部门计数
	 */
	int countChildByIdArray(Long[] array);

	/**
	 * 只读计数
	 */
	int countReadOnlyByIdArray(Long[] array);

	/**
	 * id列表查询
	 */
	List<SysDept> queryByIdArray(Long[] array);

	/**
	 * 删除
	 */
	int deleteByIdArray(Long[] array);

	/**
	 * 修改只读
	 */
	void changeReadonly(SysDept sysDept);

	/**
	 * 删除上级部门关系
	 */
	void deleteDeptParentsByIdArray(Long[] array);

	/**
	 * 删除部门用户关系
	 */
	void deleteDeptUserByIdArray(Long[] array);

	/**
	 * 获取部门岗位
	 */
	List<SysDeptPost> getPostsById(Long deptId);

	/**
	 * 删除部门岗位
	 */
	void deleteDeptPosts(Long deptId);

	/**
	 * 插入部门岗位
	 */
	void insertDeptPosts(List<SysDeptPost> list);

	/**
	 * 部门人员，id获取
	 */
	List<SysUserDept> getUsersById(Long deptId);

	/**
	 * 删除部门人员
	 */
	void deleteDeptUsers(Long deptId);

	/**
	 * 插入部门人员
	 */
	void insertDeptUsers(List<SysUserDept> list);

	/**
	 * 部门人员，code获取
	 */
	List<SysUser> getUsersByCode(String deptCode);
}
