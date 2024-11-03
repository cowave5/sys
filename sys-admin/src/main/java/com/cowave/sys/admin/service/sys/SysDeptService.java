/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.sys;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.model.admin.SysDept;
import com.cowave.sys.model.admin.SysDeptPost;
import com.cowave.sys.model.admin.SysUser;
import com.cowave.sys.model.admin.SysUserDept;

/**
 *
 * @author shanhuiming
 *
 */
public interface SysDeptService{

	/**
	 * 列表
	 */
	Page<SysDept> list(SysDept sysDept);

	/**
	 * 详情
	 */
	SysDept info(Long deptId);

	/**
	 * 新增
	 */
	void add(SysDept sysDept) throws Exception;

	/**
	 * 修改
	 */
	SysDept edit(SysDept sysDept) throws Exception;

	/**
	 * 删除
	 */
	List<SysDept> delete(Long[] deptId) throws Exception;

	/**
	 * 修改只读
	 */
	void changeReadonly(SysDept sysDept);

	/**
	 * 获取部门岗位
	 */
	List<SysDeptPost> getPostsById(Long deptId);

	/**
	 * 设置部门岗位
	 */
	void setPosts(List<SysDeptPost> list);

	/**
	 * 部门人员，id获取
	 */
	List<SysUserDept> getUsersById(Long deptId);

	/**
	 * 设置部门人员
	 */
	void setUsers(List<SysUserDept> list);

	/**
	 * 部门人员，code获取
	 */
	List<SysUser> getUsersByCode(String deptCode);
}
