/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.controller;

import com.alibaba.excel.EasyExcel;
import com.cowave.commons.framework.support.excel.CellWidthHandler;
import com.cowave.commons.response.Response;
import com.cowave.sys.admin.core.SysDictHelper;
import com.cowave.sys.admin.core.entity.dto.SysDictDto;
import com.cowave.sys.admin.core.entity.vo.SelectOption;
import com.cowave.sys.admin.core.service.SysDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 字典
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dict")
public class SysDictController {

	private final SysDictHelper sysDictHelper;

	private final SysDictService sysDictService;

	/**
	 * 列表
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:query')")
	@GetMapping("/list")
	public Response<Response.Page<SysDictDto>> list(String groupCode, String typeCode) {
		return Response.page(sysDictService.list(groupCode, typeCode));
	}

	/**
	 * 详情
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:query')")
	@GetMapping(value = "/info/{dictId}")
	public Response<SysDictDto> info(@PathVariable Long dictId) {
		return Response.success(sysDictService.info(dictId));
	}

	/**
	 * 新增
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:create')")
	@PostMapping("/add")
	public Response<Void> add(@RequestBody SysDictDto sysDict) {
		sysDictService.add(sysDict);
		return Response.success();
	}

	/**
	 * 修改
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:edit')")
	@PostMapping("/edit")
	public Response<Void> edit(@RequestBody SysDictDto sysDict) {
		sysDictService.edit(sysDict);
		return Response.success();
	}

	/**
	 * 删除
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:delete')")
	@GetMapping("/delete")
	public Response<Void> delete(@NotNull(message = "{dict.notnull.id}") Long[] dictId) {
		sysDictService.delete(dictId);
		return Response.success();
	}

	/**
	 * 修改只读
	 */
	@PreAuthorize("@permit.hasPermit('sys:common:readonly')")
	@PostMapping("/change/readonly")
	public Response<Void> changeReadonly(@RequestBody SysDictDto sysDict) {
		sysDictService.changeReadonly(sysDict);
		return Response.success();
	}

	/**
	 * 导出
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:export')")
	@RequestMapping ("/export")
	public void export(HttpServletResponse response) throws IOException {
		String fileName = URLEncoder.encode("字典数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), SysDictDto.class)
		.sheet("字典数据").registerWriteHandler(new CellWidthHandler()).doWrite(sysDictService.list(null, null).getRecords());
	}

	/**
	 * 刷新缓存
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:cache')")
	@GetMapping("/refresh")
	public Response<Void> refresh() throws Exception {
		sysDictHelper.refresh();
		return Response.success();
	}

	/**
	 * 获取字典
	 */
	@GetMapping("/cache/dict")
	public Response<SysDictDto> cacheDict(String dictCode) {
		return Response.success(sysDictHelper.getDict(dictCode));
	}

	/**
	 * 获取类型字典
	 */
	@GetMapping("/cache/type")
	public Response<Response.Page<SysDictDto>> cacheType(String typeCode) {
		return Response.page(sysDictHelper.getType(typeCode));
	}

	/**
	 * 获取分组字典
	 */
	@GetMapping("/cache/group")
	public Response<Response.Page<SysDictDto>> cacheGroup(String groupCode) {
		return Response.page(sysDictHelper.getGroup(groupCode));
	}

	/**
	 * 字典类型选项
	 */
	@GetMapping("/options")
	public Response<List<SelectOption>> dictOptions() {
		return Response.success(sysDictService.dictOptions());
	}

	/**
	 * Group子类型选项
	 */
	@GetMapping("/group/options")
	public Response<List<SelectOption>> groupOptions(@NotNull(message = "{dict.notnull.groupcode}") String groupCode) {
		return Response.success(sysDictService.groupOptions(groupCode));
	}
}
