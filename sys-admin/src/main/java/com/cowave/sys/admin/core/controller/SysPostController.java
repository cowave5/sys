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

import cn.hutool.core.lang.tree.Tree;
import com.alibaba.excel.EasyExcel;
import com.cowave.commons.framework.access.operation.Operation;
import com.cowave.commons.framework.support.excel.CellWidthHandler;
import com.cowave.commons.response.Response;
import com.cowave.sys.admin.core.entity.dto.SysPostDto;
import com.cowave.sys.admin.core.entity.dto.SysUserDto;
import com.cowave.sys.admin.core.service.SysPostService;
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

import static com.cowave.sys.admin.core.SysOperationHandler.*;

/**
 * 岗位
 * @order 2
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
public class SysPostController {

	private final SysPostService sysPostService;

	/**
	 * 刷新缓存
	 */
	@PreAuthorize("@permit.hasPermit('sys:post:cache')")
	@GetMapping("/refresh")
	public Response<Void> refresh() throws Exception {
		sysPostService.refreshPostTree();
		return Response.success();
	}

	/**
	 * 岗位关系
	 */
	@PreAuthorize("@permit.hasPermit('sys:post:diagram')")
	@GetMapping("/tree")
	public Response<Tree<String>> tree() {
		return Response.success(sysPostService.getPostTree());
	}

	/**
	 * 部门岗位树
	 */
	@GetMapping("/tree/dept")
	public Response<List<Tree<String>>> deptPostTree() {
		return Response.success(List.of(sysPostService.getDeptPostTree()));
	}

	/**
	 * 列表
	 */
	@PreAuthorize("@permit.hasPermit('sys:post:query')")
	@PostMapping("/list")
	public Response<Response.Page<SysPostDto>> list(@RequestBody SysPostDto sysPost) {
		return Response.page(sysPostService.list(sysPost));
	}

	/**
	 * 详情
	 * @param postId 岗位id
	 */
	@PreAuthorize("@permit.hasPermit('sys:post:query')")
	@GetMapping(value = {"/info/{postId}"})
	public Response<SysPostDto> info(@PathVariable Integer postId) {
		return Response.success(sysPostService.info(postId));
	}

	/**
	 * 新增
	 */
	@Operation(type = "admin_post", action = "create", desc = "新增岗位：#{#sysPost.postName}", flag = REQ)
	@PreAuthorize("@permit.hasPermit('sys:post:create')")
	@PostMapping("/add")
	public Response<Void> add(@Validated @RequestBody SysPostDto sysPost) {
		sysPostService.add(sysPost);
		return Response.success();
	}

	/**
	 * 修改
	 */
	@Operation(type = "admin_post", action = "edit", desc = "修改岗位：#{#sysPost.postName}", flag = ALL)
	@PreAuthorize("@permit.hasPermit('sys:post:edit')")
	@PostMapping("/edit")
	public Response<SysPostDto> edit(@Validated @RequestBody SysPostDto sysPost) {
		return Response.success(sysPostService.edit(sysPost));
	}

	/**
	 * 删除
	 *
	 * @param postId 岗位id
	 */
	@Operation(type = "admin_post", action = "delete", desc = "删除岗位", flag = RESP)
	@PreAuthorize("@permit.hasPermit('sys:post:delete')")
	@GetMapping("/delete")
	public Response<List<SysPostDto>> delete(@NotNull(message = "{post.notnull.id}") Long[] postId) {
		return Response.success(sysPostService.delete(postId));
	}

	/**
	 * 修改只读
	 */
	@Operation(type = "admin_post", action = "readonly", desc = "修改岗位[#{#sysPost.postName}]只读状态", flag = REQ)
	@PreAuthorize("@permit.hasPermit('sys:common:readonly')")
	@PostMapping("/change/readonly")
	public Response<Void> changeReadonly(@RequestBody SysPostDto sysPost) {
		sysPostService.changeReadonly(sysPost);
		return Response.success();
	}

	/**
	 * 导出
	 */
	@PreAuthorize("@permit.hasPermit('sys:post:export')")
	@RequestMapping("/export")
	public void export(HttpServletResponse response) throws IOException {
		String fileName = URLEncoder.encode("岗位数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), SysPostDto.class)
		.sheet("岗位").registerWriteHandler(new CellWidthHandler()).doWrite(sysPostService.list(new SysPostDto()).getRecords());
	}

	/**
	 * 岗位人员，code获取
	 * @param postCode 岗位编码
	 */
	@GetMapping("/users/code/{postCode}")
	public Response<List<SysUserDto>> getUsersByCode(@PathVariable String postCode) {
		return Response.success(sysPostService.getUsersByCode(postCode));
	}
}
