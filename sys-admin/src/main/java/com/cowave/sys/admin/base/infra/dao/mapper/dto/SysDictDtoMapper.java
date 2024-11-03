/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.base.infra.dao.mapper.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.base.domain.vo.SelectOption;
import com.cowave.sys.admin.base.domain.dto.SysDictDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface SysDictDtoMapper {

	/**
	 * 字典类型选项
	 */
	List<SelectOption> dictOptions();

	/**
	 * Group子类型选项
	 */
	List<SelectOption> groupOptions(String groupCode);

	/**
	 * 列表
	 */
	Page<SysDictDto> list(Page<SysDictDto> page, @Param("groupCode") String groupCode, @Param("typeCode") String typeCode);

	/**
	 * 通过id查询字典
	 */
	SysDictDto queryById(long id);

	/**
	 * 新增
	 */
	void insert(SysDictDto sysDict);

	/**
	 * 修改
	 */
	void update(SysDictDto sysDict);

	/**
	 * 删除
	 */
	void delete(Long dictId);

	/**
	 * 删除类型
	 */
	void deleteType(String typeCode);

	/**
	 * 删除分组
	 */
	void deleteGroup(String groupCode);

	/**
	 * 修改只读
	 */
	void changeReadonly(SysDictDto sysDict);

	/**
	 * 字典查询
	 */
	List<SysDictDto> queryByIdArray(Long[] array);

	/**
	 * 子字典查询
	 */
	List<SysDictDto> queryByParentCode(String parentCode);

	/**
	 * 更新字典类型码
	 */
	void updateChildren(@Param("parentCode") String parentCode, @Param("preCode") String preCode);

	/**
	 * 计数 只读字典
	 */
	int countReadOnlyByIdArray(Long[] array);
}
