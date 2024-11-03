/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.base.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.Asserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.base.infra.SysDictHelper;
import com.cowave.sys.admin.base.infra.dao.mapper.dto.SysDictDtoMapper;
import com.cowave.sys.admin.base.domain.dto.SysDictDto;
import com.cowave.sys.admin.base.domain.vo.SelectOption;
import com.cowave.sys.admin.base.service.SysDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class SysDictServiceImpl implements SysDictService {

	private final SysDictHelper sysDictHelper;

	private final SysDictDtoMapper sysDictMapper;

	@Override
	public List<SelectOption> dictOptions() {
		return sysDictMapper.dictOptions();
	}

	@Override
	public List<SelectOption> groupOptions(String groupCode) {
		return sysDictMapper.groupOptions(groupCode);
	}

	@Override
	public Page<SysDictDto> list(String groupCode, String typeCode){
		return sysDictMapper.list(Access.page(), groupCode, typeCode);
	}

	@Override
	public SysDictDto info(Long dictId){
		return sysDictMapper.queryById(dictId);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void add(SysDictDto sysDict){
		sysDictMapper.insert(sysDict);
		sysDict = sysDictMapper.queryById(sysDict.getId());
		sysDictHelper.put(sysDict);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void edit(SysDictDto sysDict){
		Asserts.notNull(sysDict.getId(), "{dict.notnull.id}");
		SysDictDto preDict = sysDictMapper.queryById(sysDict.getId());
		Asserts.notNull(preDict, "{dict.notexist}", sysDict.getId());
		Asserts.notEquals(1, preDict.getReadOnly(), "{dict.forbid.edit.readonly}");

		sysDictMapper.update(sysDict);
		sysDictHelper.put(sysDictMapper.queryById(sysDict.getId()));
		if (!preDict.getDictCode().equals(sysDict.getDictCode())) {
			sysDictHelper.removeDict(preDict.getDictCode());
			// 组字典
			if ("dict_root".equals(preDict.getGroupCode())) {
				sysDictMapper.updateChildren(sysDict.getDictCode(), preDict.getDictCode());
				sysDictHelper.removeGroup(preDict.getDictCode());
				List<SysDictDto> list = sysDictMapper.list(
						Access.page(Integer.MAX_VALUE), sysDict.getDictCode(), null).getRecords();
				list.addAll(sysDictMapper.queryByParentCode(sysDict.getDictCode()));
				for (SysDictDto dict : list) {
					sysDictHelper.put(dict);
				}
			}
			// 类型字典
			if ("dict_group".equals(preDict.getGroupCode())) {
				sysDictMapper.updateChildren(sysDict.getDictCode(), preDict.getDictCode());
				sysDictHelper.removeType(preDict.getDictCode());
				List<SysDictDto> list = sysDictMapper.list(
						Access.page(Integer.MAX_VALUE), null, sysDict.getDictCode()).getRecords();
				for (SysDictDto dict : list) {
					sysDictHelper.put(dict);
				}
			}
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(Long[] dictId){
		Asserts.isTrue(sysDictMapper.countReadOnlyByIdArray(dictId) == 0, "{dict.forbid.delete.readonly}");
		List<SysDictDto> list = sysDictMapper.queryByIdArray(dictId);
		for(SysDictDto dict : list){
			delete(dict);
		}
	}

	private void delete(SysDictDto dict){
		sysDictMapper.delete(dict.getId());
		sysDictHelper.removeDict(dict.getDictCode());
		// 组字典
		if(dict.getGroupCode().equals("dict_root")){
			sysDictMapper.deleteGroup(dict.getDictCode());
			sysDictHelper.removeGroup(dict.getDictCode());
		}
		// 类型字典
		if(dict.getGroupCode().equals("dict_group")){
			sysDictMapper.deleteType(dict.getDictCode());
			sysDictHelper.removeType(dict.getDictCode());
		}
	}

	@Override
	public void changeReadonly(SysDictDto sysDict) {
		sysDictMapper.changeReadonly(sysDict);
	}
}
