/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.service;

import cn.hutool.core.lang.tree.Tree;
import com.cowave.commons.response.Response;
import com.cowave.sys.admin.core.entity.dto.*;
import com.cowave.sys.admin.core.entity.request.DeptCreate;
import com.cowave.sys.admin.core.entity.request.DeptQuery;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysDeptService{

	/**
	 * 列表
	 */
	List<SysDeptListDto> list(DeptQuery deptQuery);

	/**
	 * 详情
	 */
	SysDeptInfoDto info(Integer deptId);

	/**
	 * 新增
	 */
	void create(DeptCreate dept) throws Exception;

	/**
	 * 修改
	 */
	SysDeptInfoDto edit(DeptCreate dept) throws Exception;

	/**
	 * 删除
	 */
	List<SysDeptDto> delete(Long[] deptId) throws Exception;

	/**
	 * 部门成员
	 */
	Response.Page<SysDeptUserDto> members(Integer deptId);

	/**
	 * 修改只读
	 */
	void changeReadonly(SysDeptDto sysDept);

	/**
	 * 获取部门岗位
	 */
	List<SysDeptPostDto> getPostsById(Integer deptId);

	/**
	 * 设置部门岗位
	 */
	void setPosts(List<SysDeptPostDto> list);

	/**
	 * 部门人员，id获取
	 */
	List<SysUserDeptDto> getUsersById(Integer deptId);

	/**
	 * 设置部门人员
	 */
	void setUsers(List<SysUserDeptDto> list);

	/**
	 * 部门人员，code获取
	 */
	List<SysUserDto> getUsersByCode(String deptCode);

	/**
     * 刷新缓存
     */
    void refreshDeptTree();

	/**
	 * 部门树
	 */
	List<Tree<String>> getDeptTree(String deptId);
}
