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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.core.entity.dto.SysPostDto;
import com.cowave.sys.admin.core.entity.dto.SysUserDto;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysPostService {

	/**
	 * 列表
	 */
	Page<SysPostDto> list(SysPostDto sysPost);

	/**
	 * 详情
	 */
	SysPostDto info(Long postId);

	/**
	 * 新增
	 */
	void add(SysPostDto sysPost);

	/**
	 * 修改
	 */
	SysPostDto edit(SysPostDto sysPost);

	/**
	 * 删除
	 */
	List<SysPostDto> delete(Long[] postId);

	/**
	 * 修改只读
	 */
	void changeReadonly(SysPostDto sysPost);

	/**
	 * 岗位人员，code获取
	 */
	List<SysUserDto> getUsersByCode(String postCode);

	/**
     * 刷新缓存
     */
    void refreshPostTree();

	/**
	 * 获取岗位树
	 */
	Tree<String> getPostTree();

	/**
	 * 获取部门岗位树
	 */
	Tree<String> getDeptPostTree();
}
