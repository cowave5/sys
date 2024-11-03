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
import com.cowave.commons.framework.support.excel.valid.ExcelImportValidListener;
import com.cowave.commons.framework.support.excel.write.DropdownWriteHandler;
import com.cowave.commons.response.Response;
import com.cowave.commons.response.exception.Messages;
import com.cowave.sys.admin.core.entity.SysUser;
import com.cowave.sys.admin.core.entity.dto.SysUserInfoDto;
import com.cowave.sys.admin.core.entity.dto.SysUserListDto;
import com.cowave.sys.admin.core.entity.dto.SysUserNameDto;
import com.cowave.sys.admin.core.entity.request.*;
import com.cowave.sys.admin.core.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.cowave.sys.admin.core.SysOperationHandler.*;

/**
 * 用户
 * @order 3
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class SysUserController {

	private final SysUserService sysUserService;

	/**
	 * 列表
	 */
	@PreAuthorize("@permit.hasPermit('sys:user:query')")
	@GetMapping
	public Response<Response.Page<SysUserListDto>> list(UserQuery query) {
		return Response.success(sysUserService.list(query));
	}

	/**
	 * 详情
	 * @param userId 用户id
	 */
	@PreAuthorize("@permit.hasPermit('sys:user:query')")
	@GetMapping(value = {"/{userId}"})
	public Response<SysUserInfoDto> info(@PathVariable Integer userId) {
		return Response.success(sysUserService.info(userId));
	}

	/**
	 * 新增
	 */
	@Operation(type = "admin_user", action = "create", desc = "新增用户：#{#user.userName}", flag = REQ)
	@PreAuthorize("@permit.hasPermit('sys:user:create')")
	@PostMapping
	public Response<Void> create(@Validated @RequestBody UserCreate user) {
		return Response.action(() -> sysUserService.create(user));
	}

	/**
	 * 修改
	 */
	@Operation(type = "admin_user", action = "edit", desc = "修改用户：#{#user.userName}", flag = ALL)
	@PreAuthorize("@permit.hasPermit('sys:user:edit')")
	@PatchMapping
	public Response<Void> edit(@Validated @RequestBody UserCreate user) {
		return Response.action(() -> sysUserService.edit(user));
	}

	/**
	 * 删除
	 * @param userIds id列表
	 */
	@Operation(type = "admin_user", action = "delete", desc = "删除用户", flag = RESP)
	@PreAuthorize("@permit.hasPermit('sys:user:delete')")
	@DeleteMapping(value = {"/{userIds}"})
	public Response<Void> delete(@PathVariable List<Integer> userIds) {
		return Response.action(() -> sysUserService.delete(userIds));
	}

	/**
	 * 修改角色
	 */
	@Operation(type = "admin_user", action = "role_grant", desc = "修改用户[#{#roleUpdate.userName}]角色")
	@PreAuthorize("@permit.hasPermit('sys:user:grant')")
	@PatchMapping("/roles")
	public Response<Void> changeRoles(@Validated @RequestBody UserRoleUpdate roleUpdate) {
		return Response.action(() -> sysUserService.changeRoles(roleUpdate));
	}

	/**
	 * 修改状态
	 */
	@Operation(type = "admin_user", action = "status", desc = "修改用户[#{#statusUpdate.userName}]状态", flag = REQ)
	@PreAuthorize("@permit.hasPermit('sys:user:edit')")
	@PatchMapping("/status")
	public Response<Void> changeStatus(@Validated @RequestBody UserStatusUpdate statusUpdate) {
		return Response.action(() -> sysUserService.changeStatus(statusUpdate));
	}

	/**
	 * 修改密码
	 */
	@Operation(type = "admin_user", action = "passwd", desc = "修改用户[#{#passwdUpdate.userName}]密码")
	@PreAuthorize("@permit.hasPermit('sys:user:passwd')")
	@PatchMapping("/passwd")
	public Response<Void> changePasswd(@Validated @RequestBody UserPasswdUpdate passwdUpdate) {
		return Response.action(() -> sysUserService.changePasswd(passwdUpdate));
	}

	/**
	 * 修改只读
	 */
	@Operation(type = "admin_user", action = "readonly", desc = "修改用户[#{#readUpdate.userName}]只读状态", flag = REQ)
	@PreAuthorize("@permit.hasPermit('sys:common:readonly')")
	@PatchMapping("/readonly")
	public Response<Void> changeReadonly(@Validated @RequestBody UserReadUpdate readUpdate) {
		return Response.action(() -> sysUserService.changeReadonly(readUpdate));
	}

	/**
	 * 导入用户
	 */
	@PreAuthorize("@permit.hasPermit('sys:user:import')")
	@PostMapping("/import")
	public Response<Void> importUser(MultipartFile file, boolean updateSupport) throws Exception {
		try(InputStream inputStream = file.getInputStream()){
			EasyExcel.read(inputStream).head(SysUser.class).registerReadListener(
					new ExcelImportValidListener<>(sysUserService, updateSupport)).sheet().doReadSync();
		}
		return Response.success(null, Messages.msg("import.success.msg"));
	}

	/**
	 * 导出用户
	 */
	@PreAuthorize("@permit.hasPermit('sys:user:export')")
	@RequestMapping("/export")
	public void export(HttpServletResponse response, UserExport userExport) throws Exception {
		String fileName = URLEncoder.encode("用户数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), SysUser.class).sheet("用户")
				.registerWriteHandler(new CellWidthHandler()).doWrite(sysUserService.getExportUsers(userExport));
	}

	/**
	 * 导出模板
	 */
	@PreAuthorize("@permit.hasPermit('sys:user:export')")
	@RequestMapping("/export/template")
	public void exportTemplate(HttpServletResponse response) throws Exception {
		String fileName = URLEncoder.encode("test", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		DropdownWriteHandler sexHandler =
				new DropdownWriteHandler(List.of("男", "女", "未知"), 3, 1000);
		DropdownWriteHandler statusHandler =
				new DropdownWriteHandler(List.of("启用", "停用"), 6, 1000);
		EasyExcel.write(response.getOutputStream(), SysUser.class)
				.sheet("用户")
				.registerWriteHandler(sexHandler)
				.registerWriteHandler(statusHandler)
				.doWrite(new ArrayList<>());
	}

	/**
	 * 刷新缓存
	 */
	@PreAuthorize("@permit.hasPermit('sys:user:cache')")
	@GetMapping("/refresh")
	public Response<Void> refresh() throws Exception {
		return Response.action(sysUserService::refreshUserTree);
	}

	/**
	 * 用户树
	 */
	@PreAuthorize("@permit.hasPermit('sys:user:diagram')")
	@GetMapping("/tree")
	public Response<Tree<String>> tree() {
		return Response.success(sysUserService.getUserTree());
	}

	/**
	 * 部门用户树
	 */
	@GetMapping("/tree/dept")
	public Response<List<Tree<String>>> deptUserTree() {
		return Response.success(List.of(sysUserService.getDeptUserTree()));
	}

	/**
	 * 候选人列表：上级用户
	 * @param userId 用户id
	 */
	@GetMapping("/leaders")
	public Response<List<SysUserNameDto>> leaders(Integer userId) {
		return Response.success(sysUserService.leaders(userId));
	}
}
