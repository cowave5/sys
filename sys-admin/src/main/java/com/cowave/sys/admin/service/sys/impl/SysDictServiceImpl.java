/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.response.exception.Asserts;
import com.cowave.sys.admin.service.sys.SysDictService;
import com.cowave.sys.admin.core.cache.SysDictCache;
import com.cowave.sys.admin.core.entity.SelectOption;
import com.cowave.sys.admin.core.mapper.SysDictMapper;
import com.cowave.sys.model.admin.SysDict;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class SysDictServiceImpl implements SysDictService {

	private final SysDictCache sysDictCache;

	private final SysDictMapper sysDictMapper;

	@Override
	public List<SelectOption> dictOptions() {
		return sysDictMapper.dictOptions();
	}

	@Override
	public List<SelectOption> groupOptions(String groupCode) {
		return sysDictMapper.groupOptions(groupCode);
	}

	@Override
	public Page<SysDict> list(String groupCode, String typeCode){
		return sysDictMapper.list(Access.page(), groupCode, typeCode);
	}

	@Override
	public SysDict info(Long dictId){
		return sysDictMapper.queryById(dictId);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void add(SysDict sysDict){
		sysDictMapper.insert(sysDict);
		sysDict = sysDictMapper.queryById(sysDict.getId());
		sysDictCache.put(sysDict);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void edit(SysDict sysDict){
		Asserts.notNull(sysDict.getId(), "{dict.notnull.id}");
		SysDict preDict = sysDictMapper.queryById(sysDict.getId());
		Asserts.notNull(preDict, "{dict.notexist}", sysDict.getId());
		Asserts.notEquals(1, preDict.getReadOnly(), "{dict.forbid.edit.readonly}");

		sysDictMapper.update(sysDict);
		sysDictCache.put(sysDictMapper.queryById(sysDict.getId()));
		if (!preDict.getDictCode().equals(sysDict.getDictCode())) {
			sysDictCache.removeDict(preDict.getDictCode());
			// 组字典
			if ("dict_root".equals(preDict.getGroupCode())) {
				sysDictMapper.updateChildren(sysDict.getDictCode(), preDict.getDictCode());
				sysDictCache.removeGroup(preDict.getDictCode());
				List<SysDict> list = sysDictMapper.list(
						Access.page(Integer.MAX_VALUE), sysDict.getDictCode(), null).getRecords();
				list.addAll(sysDictMapper.queryByParentCode(sysDict.getDictCode()));
				for (SysDict dict : list) {
					sysDictCache.put(dict);
				}
			}
			// 类型字典
			if ("dict_group".equals(preDict.getGroupCode())) {
				sysDictMapper.updateChildren(sysDict.getDictCode(), preDict.getDictCode());
				sysDictCache.removeType(preDict.getDictCode());
				List<SysDict> list = sysDictMapper.list(
						Access.page(Integer.MAX_VALUE), null, sysDict.getDictCode()).getRecords();
				for (SysDict dict : list) {
					sysDictCache.put(dict);
				}
			}
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(Long[] dictId){
		Asserts.equals(0, sysDictMapper.countReadOnlyByIdArray(dictId), "{dict.forbid.delete.readonly}");
		List<SysDict> list = sysDictMapper.queryByIdArray(dictId);
		for(SysDict dict : list){
			delete(dict);
		}
	}

	private void delete(SysDict dict){
		sysDictMapper.delete(dict.getId());
		sysDictCache.removeDict(dict.getDictCode());
		// 组字典
		if(dict.getGroupCode().equals("dict_root")){
			sysDictMapper.deleteGroup(dict.getDictCode());
			sysDictCache.removeGroup(dict.getDictCode());
		}
		// 类型字典
		if(dict.getGroupCode().equals("dict_group")){
			sysDictMapper.deleteType(dict.getDictCode());
			sysDictCache.removeType(dict.getDictCode());
		}
	}

	@Override
	public void changeReadonly(SysDict sysDict) {
		sysDictMapper.changeReadonly(sysDict);
	}
}
