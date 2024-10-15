/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.api.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.model.admin.SysPost;
import com.cowave.sys.model.admin.SysUser;

/**
 *
 * @author shanhuiming
 *
 */
public interface SysPostService {

	/**
	 * 列表
	 */
	Page<SysPost> list(SysPost sysPost);

	/**
	 * 详情
	 */
	SysPost info(Long postId);

	/**
	 * 新增
	 */
	void add(SysPost sysPost);

	/**
	 * 修改
	 */
	SysPost edit(SysPost sysPost);

	/**
	 * 删除
	 */
	List<SysPost> delete(Long[] postId);

	/**
	 * 修改只读
	 */
	void changeReadonly(SysPost sysPost);

	/**
	 * 岗位人员，code获取
	 */
	List<SysUser> getUsersByCode(String postCode);
}
