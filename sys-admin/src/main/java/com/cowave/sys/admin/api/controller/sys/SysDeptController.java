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

import cn.hutool.core.lang.tree.Tree;
import com.alibaba.excel.EasyExcel;
import com.cowave.commons.framework.access.operation.Operation;
import com.cowave.commons.framework.support.excel.CellWidthHandler;
import com.cowave.sys.admin.api.service.SysDeptService;
import com.cowave.sys.admin.core.OplogHandler;
import com.cowave.sys.admin.core.caches.SysDeptCaches;
import com.cowave.sys.model.admin.SysDept;
import com.cowave.sys.model.admin.SysDeptPost;
import com.cowave.sys.model.admin.SysUser;
import com.cowave.sys.model.admin.SysUserDept;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.feign.codec.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 部门
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dept")
public class SysDeptController {

    private final SysDeptCaches sysDeptCaches;

    private final SysDeptService sysDeptService;

    /**
	 * 刷新缓存
	 */
    @PreAuthorize("@permit.hasPermit('sys:dept:cache')")
	@GetMapping("/refresh")
	public Response<Void> refresh() {
        sysDeptCaches.refreshTree();
		return Response.success();
	}

    /**
     * 树
     */
	@GetMapping("/tree")
    public Response<List<Tree<String>>> tree(String deptId) {
        return Response.success(sysDeptCaches.tree(deptId));
    }

    /**
     * 列表
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:query')")
	@PostMapping("/list")
    public Response<Response.Page<SysDept>> list(@RequestBody SysDept sysDept) {
        return Response.page(sysDeptService.list(sysDept));
    }

    /**
     * 详情
     * @param deptId 部门id
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:query')")
    @GetMapping("/info/{deptId}")
    public Response<SysDept> info(@PathVariable Long deptId) {
        return Response.success(sysDeptService.info(deptId));
    }

    /**
     * 新增
     */
    @Operation(type = "admin_dept", action = "add", handler = OplogHandler.class,
            summary = "新增部门：#{#sysDept.deptName}", expr = "#opHandler.logRequest(#opInfo)")
    @PreAuthorize("@permit.hasPermit('sys:dept:new')")
    @PostMapping("/add")
    public Response<Void> add(@Validated @RequestBody SysDept sysDept) throws Exception {
        sysDeptService.add(sysDept);
        return Response.success();
    }

    /**
     * 修改
     */
    @Operation(type = "admin_dept", action = "edit", handler = OplogHandler.class,
            summary = "修改部门：#{#resp.deptName}", expr = "#opHandler.logAll(#opInfo, #sysDept, #resp)")
    @PreAuthorize("@permit.hasPermit('sys:dept:edit')")
    @PostMapping("/edit")
    public Response<SysDept> edit(@RequestBody SysDept sysDept) throws Exception {
        return Response.success(sysDeptService.edit(sysDept));
    }

    /**
     * 删除
     * @param deptId 部门id
     */
    @Operation(type = "admin_dept", action = "delete", handler = OplogHandler.class,
            summary = "删除部门", expr = "#opHandler.logResponse(#opInfo, #resp)")
    @PreAuthorize("@permit.hasPermit('sys:dept:delete')")
    @GetMapping("/delete")
    public Response<List<SysDept>> delete(@NotNull(message = "{dept.notnull.id}") Long[] deptId) throws Exception {
        return Response.success(sysDeptService.delete(deptId));
    }

    /**
     * 导出
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:export')")
    @RequestMapping ("/export")
    public void export(HttpServletResponse response) throws IOException {
    	String sheet = "部门";
    	String excel = "部门数据";
		List<SysDept> list = sysDeptService.list(new SysDept()).getRecords();
		if(CollectionUtils.isNotEmpty(list)) {
			sheet = list.get(0).getDeptName();
			excel = "部门-" + sheet;
		}
    	String fileName = URLEncoder.encode(excel, StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), SysDept.class)
		.sheet(sheet).registerWriteHandler(new CellWidthHandler()).doWrite(list);
    }

    /**
     * 修改只读
     */
    @Operation(type = "admin_dept", action = "readonly", handler = OplogHandler.class,
            summary = "修改部门[#{#sysDept.deptName}]只读状态", expr = "#opHandler.logRequest(#opInfo)")
    @PreAuthorize("@permit.hasPermit('sys:common:readonly')")
    @PostMapping("/change/readonly")
    public Response<Void> changeReadonly(@RequestBody SysDept sysDept) {
        sysDeptService.changeReadonly(sysDept);
        return Response.success();
    }

    /**
     * 获取部门岗位
     * @param deptId 部门id
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:query')")
    @GetMapping("/posts/id/{deptId}")
    public Response<List<SysDeptPost>> getPostsById(@PathVariable Long deptId) {
        return Response.success(sysDeptService.getPostsById(deptId));
    }

    /**
     * 设置部门岗位
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:positions')")
    @PostMapping("/posts/set")
    public Response<Void> setPosts(@Valid @RequestBody List<SysDeptPost> list) {
        sysDeptService.setPosts(list);
        return Response.success();
    }

    /**
     * 部门人员，id获取
     * @param deptId 部门id
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:query')")
    @GetMapping("/users/id/{deptId}")
    public Response<List<SysUserDept>> getUsersById(@PathVariable Long deptId) {
        return Response.success(sysDeptService.getUsersById(deptId));
    }

    /**
     * 设置部门人员
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:members')")
    @PostMapping("/users/set")
    public Response<Void> setUsers(@Valid @RequestBody List<SysUserDept> list) {
        sysDeptService.setUsers(list);
        return Response.success();
    }

    /**
     * 部门人员，code获取
     * @param deptCode 部门编码
     */
    @GetMapping("/users/code/{deptCode}")
    public Response<List<SysUser>> getUsersByCode(@PathVariable String deptCode) {
        return Response.success(sysDeptService.getUsersByCode(deptCode));
    }
}
