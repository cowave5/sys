/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.base.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.base.domain.vo.SelectOption;
import com.cowave.sys.admin.base.domain.dto.SysDictDto;

/**
 * @author shanhuiming
 */
public interface SysDictService{

	/**
	 * 类型选项
	 */
	List<SelectOption> dictOptions();

	/**
	 * Group子类型选项
	 */
	List<SelectOption> groupOptions(String groupCode);

	/**
	 * 列表
	 */
	Page<SysDictDto> list(String groupCode, String typeCode);

	/**
	 * 详情
	 */
	SysDictDto info(Long dictId);

	/**
	 * 新增
	 */
	void add(SysDictDto sysDict);

	/**
	 * 修改
	 */
	void edit(SysDictDto sysDict);

	/**
	 * 删除
	 */
	void delete(Long[] dictId);

	/**
	 * 修改只读
	 */
	void changeReadonly(SysDictDto sysDict);
}
