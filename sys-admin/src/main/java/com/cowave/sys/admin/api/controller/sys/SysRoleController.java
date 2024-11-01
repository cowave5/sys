/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.api.controller.sys;

import com.alibaba.excel.EasyExcel;
import com.cowave.commons.framework.access.operation.Operation;
import com.cowave.commons.framework.support.excel.CellWidthHandler;
import com.cowave.sys.admin.api.service.SysRoleService;
import com.cowave.sys.admin.core.OplogHandler;
import com.cowave.sys.admin.core.entity.UserAuthed;
import com.cowave.sys.model.admin.SysRole;
import com.cowave.sys.model.admin.SysUser;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
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
 * 角色
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/role")
public class SysRoleController {

    private final SysRoleService sysRoleService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public Response<Response.Page<SysRole>> list(@RequestBody SysRole sysRole) {
        return Response.page(sysRoleService.list(sysRole));
    }

    /**
     * 详情
     *
     * @param roleId 角色id
     */
    @GetMapping(value = "/info/{roleId}")
    public Response<SysRole> info(@PathVariable Long roleId) {
        return Response.success(sysRoleService.info(roleId));
    }

    /**
     * 新增
     */
    @Operation(type = "admin_role", action = "add", handler = OplogHandler.class,
            summary = "新增角色：#{#sysRole.roleName}", expr = "#opHandler.logRequest(#opInfo)")
    @PostMapping("/add")
    public Response<Void> add(@Validated @RequestBody SysRole sysRole) {
    	sysRoleService.add(sysRole);
        return Response.success();
    }

    /**
     * 修改
     */
    @Operation(type = "admin_role", action = "edit", handler = OplogHandler.class,
            summary = "修改角色：#{#sysRole.roleName}", expr = "#opHandler.logAll(#opInfo, #sysRole, #resp)")
    @PostMapping("/edit")
    public Response<SysRole> edit(@Validated @RequestBody SysRole sysRole) {
        return Response.success(sysRoleService.edit(sysRole));
    }

    /**
     * 删除
     *
     * @param roleId 角色id
     */
    @Operation(type = "admin_role", action = "delete", handler = OplogHandler.class,
            summary = "删除角色", expr = "#opHandler.logResponse(#opInfo, #resp)")
    @GetMapping("/delete")
    public Response<List<SysRole>> delete(@NotNull(message = "{role.notnull.id}") Long[] roleId) {
        return Response.success(sysRoleService.delete(roleId));
    }

    /**
     * 修改只读
     */
    @Operation(type = "admin_role", action = "readonly", handler = OplogHandler.class,
            summary = "修改角色[#{#sysRole.roleName}]只读状态", expr = "#opHandler.logRequest(#opInfo)")
    @PreAuthorize("@permit.hasPermit('sys:common:readonly')")
    @PostMapping("/change/readonly")
    public Response<Void> changeReadonly(@RequestBody SysRole sysRole) {
        sysRoleService.changeReadonly(sysRole);
        return Response.success();
    }

    /**
     * 导出
     */
    @RequestMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
    	String fileName = URLEncoder.encode("角色数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), SysRole.class)
		.sheet("角色").registerWriteHandler(new CellWidthHandler()).doWrite(sysRoleService.list(new SysRole()).getRecords());
    }

    /**
     * 修改角色菜单
     */
    @PostMapping("/change/menus")
    public Response<Void> changeMenus(@RequestBody SysRole sysRole) {
        sysRoleService.changeMenus(sysRole);
        return Response.success();
    }

    /**
     * 已授权用户
     */
    @PostMapping("/user/authed")
    public Response<Response.Page<SysUser>> userAuthed(@Validated @RequestBody UserAuthed userAuthed) {
    	return Response.page(sysRoleService.userAuthed(userAuthed));
    }

    /**
     * 未授权用户
     */
    @PostMapping("/user/unauthed")
    public Response<Response.Page<SysUser>> userUnAuthed(@Validated @RequestBody UserAuthed userAuthed) {
    	return Response.page(sysRoleService.userUnAuthed(userAuthed));
    }

    /**
     * 授予用户角色
     */
    @PostMapping("/user/grant")
    public Response<Void> grant(@Validated @RequestBody UserAuthed userAuthed) {
        sysRoleService.grant(userAuthed);
    	return Response.success();
    }

    /**
     * 取消用户角色
     */
    @PostMapping("/user/cancel")
    public Response<Void> cancel(@Validated @RequestBody UserAuthed userAuthed) {
        sysRoleService.cancel(userAuthed);
    	return Response.success();
    }
}
