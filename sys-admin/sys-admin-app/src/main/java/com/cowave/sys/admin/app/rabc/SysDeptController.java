/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.app.rabc;

import cn.hutool.core.lang.tree.Tree;
import com.alibaba.excel.EasyExcel;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.operation.Operation;
import com.cowave.commons.framework.support.excel.CellWidthHandler;
import com.cowave.sys.admin.domain.rabc.SysDept;
import com.cowave.sys.admin.domain.rabc.request.DeptCreate;
import com.cowave.sys.admin.domain.rabc.request.DeptQuery;
import com.cowave.sys.admin.domain.rabc.request.DeptReadUpdate;
import com.cowave.sys.admin.service.rabc.SysDeptService;
import com.cowave.sys.admin.domain.rabc.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.cowave.sys.admin.infra.base.SysOperationHandler.*;

/**
 * 部门
 *
 * @author shanhuiming
 * @order 1
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dept")
public class SysDeptController {

    private final SysDeptService sysDeptService;

    /**
     * 列表
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:query')")
    @GetMapping
    public Response<Response.Page<SysDeptListDto>> list(DeptQuery deptQuery) {
        return Response.page(sysDeptService.list(deptQuery));
    }

    /**
     * 详情
     *
     * @param deptId 部门id
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:query')")
    @GetMapping("/{deptId}")
    public Response<SysDeptInfoDto> info(@PathVariable Integer deptId) {
        return Response.success(sysDeptService.info(deptId));
    }

    /**
     * 新增
     */
    @Operation(type = "admin_dept", action = "create", desc = "新增部门：#{#dept.deptName}", flag = REQ)
    @PreAuthorize("@permit.hasPermit('sys:dept:create')")
    @PostMapping
    public Response<Void> create(@Validated @RequestBody DeptCreate dept) throws Exception {
        return Response.success(() -> sysDeptService.create(dept));
    }

    /**
     * 修改
     */
    @Operation(type = "admin_dept", action = "edit", desc = "修改部门：#{#resp.deptName}", flag = ALL)
    @PreAuthorize("@permit.hasPermit('sys:dept:edit')")
    @PatchMapping
    public Response<Void> edit(@RequestBody DeptCreate dept) throws Exception {
        return Response.success(() -> sysDeptService.edit(dept));
    }

    /**
     * 删除
     *
     * @param deptIds 部门id列表
     */
    @Operation(type = "admin_dept", action = "delete", desc = "删除部门", flag = RESP)
    @PreAuthorize("@permit.hasPermit('sys:dept:delete')")
    @DeleteMapping("/{deptIds}")
    public Response<Void> delete(@PathVariable List<Integer> deptIds) throws Exception {
        return Response.success(() -> sysDeptService.delete(deptIds));
    }

    /**
     * 导出部门
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:export')")
    @RequestMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        String sheet = "部门";
        String excel = "部门数据";
        List<SysDept> list = sysDeptService.listForExport();
        if (CollectionUtils.isNotEmpty(list)) {
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
    @Operation(type = "admin_dept", action = "readonly", desc = "修改部门[#{#readUpdate.deptName}]只读状态", flag = REQ)
    @PreAuthorize("@permit.hasPermit('sys:common:readonly')")
    @PatchMapping("/readonly")
    public Response<Void> changeReadonly(@RequestBody DeptReadUpdate readUpdate) throws Exception {
        return Response.success(() -> sysDeptService.changeReadonly(readUpdate));
    }

    /**
     * 获取部门岗位
     *
     * @param deptId 部门id
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:query')")
    @GetMapping("/posts/{deptId}")
    public Response<List<SysDeptPostDto>> getPostsById(@PathVariable Integer deptId) {
        return Response.success(sysDeptService.getPostsById(deptId));
    }

    /**
     * 设置部门岗位
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:positions')")
    @PostMapping("/posts/set")
    public Response<Void> setPosts(@Valid @RequestBody List<SysDeptPostDto> list) throws Exception {
        return Response.success(() -> sysDeptService.setPosts(list));
    }

    /**
     * 获取部门成员
     *
     * @param deptId 部门id
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:query')")
    @GetMapping("/members/{deptId}")
    public Response<Response.Page<SysDeptUserDto>> members(@PathVariable Integer deptId) {
        return Response.success(sysDeptService.members(deptId));
    }

    /**
     * id获取部门人员
     *
     * @param deptId 部门id
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:query')")
    @GetMapping("/users/id/{deptId}")
    public Response<List<SysUserDeptDto>> getUsersById(@PathVariable Integer deptId) {
        return Response.success(sysDeptService.getUsersById(deptId));
    }

    /**
     * 设置部门人员
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:members')")
    @PostMapping("/users/set")
    public Response<Void> setUsers(@Valid @RequestBody List<SysUserDeptDto> list) throws Exception {
        return Response.success(() -> sysDeptService.setUsers(list));
    }

    /**
     * code获取部门人员
     *
     * @param deptCode 部门编码
     */
    @GetMapping("/users/code/{deptCode}")
    public Response<List<SysUserDto>> getUsersByCode(@PathVariable String deptCode) {
        return Response.success(sysDeptService.getUsersByCode(deptCode));
    }

    /**
     * 部门树
     */
    @GetMapping("/tree")
    public Response<List<Tree<String>>> tree(String deptId) {
        return Response.success(sysDeptService.getDeptTree(deptId));
    }

    /**
     * 刷新缓存
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:cache')")
    @GetMapping("/refresh")
    public Response<Void> refresh() throws Exception {
        return Response.success(sysDeptService::refreshDeptTree);
    }
}
