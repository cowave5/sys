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
import com.cowave.sys.admin.domain.rabc.SysDeptPost;
import com.cowave.sys.admin.domain.rabc.SysUserDept;
import com.cowave.sys.admin.domain.rabc.dto.*;
import com.cowave.sys.admin.domain.rabc.request.*;
import com.cowave.sys.admin.service.rabc.SysDeptService;
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
    public Response<Response.Page<DeptListDto>> list(DeptQuery query) {
        return Response.page(sysDeptService.list(query));
    }

    /**
     * 详情
     *
     * @param deptId 部门id
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:query')")
    @GetMapping("/{deptId}")
    public Response<DeptInfoDto> info(@PathVariable Integer deptId) {
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
     * 修改
     */
    @Operation(type = "admin_dept", action = "edit", desc = "修改部门：#{#resp.deptName}", flag = ALL)
    @PreAuthorize("@permit.hasPermit('sys:dept:edit')")
    @PatchMapping
    public Response<Void> edit(@RequestBody DeptCreate dept) throws Exception {
        return Response.success(() -> sysDeptService.edit(dept));
    }

    /**
     * 修改只读
     */
    @Operation(type = "admin_dept", action = "readonly", desc = "修改部门[#{#deptUpdate.deptName}]只读状态", flag = REQ)
    @PreAuthorize("@permit.hasPermit('sys:common:readonly')")
    @PatchMapping("/readonly")
    public Response<Void> updateReadonly(@Validated @RequestBody DeptReadUpdate deptUpdate) throws Exception {
        return Response.success(() -> sysDeptService.updateReadonly(deptUpdate));
    }

    /**
     * 导出部门
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:export')")
    @PostMapping("/export")
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
     * 部门组织架构
     */
    @GetMapping("/diagram")
    public Response<List<Tree<String>>> getDiagram(String deptId) {
        return Response.success(sysDeptService.getDiagram(deptId));
    }

    /**
     * 刷新部门组织
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:cache')")
    @GetMapping("/diagram/refresh")
    public Response<Void> refreshDiagram() throws Exception {
        return Response.success(sysDeptService::refreshDiagram);
    }

    /**
	 * 部门岗位树
	 */
	@GetMapping("/diagram/post")
	public Response<List<Tree<String>>> getPostDiagram() {
		return Response.success(List.of(sysDeptService.getPostDiagram()));
	}

    /**
     * 部门用户树
     */
    @GetMapping("/diagram/user")
    public Response<List<Tree<String>>> getUserDiagram() {
        return Response.success(List.of(sysDeptService.getUserDiagram()));
    }

    /**
     * 添加部门岗位
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:positions')")
    @PostMapping("/posts")
    public Response<Void> addPosts(@Valid @RequestBody List<SysDeptPost> list) throws Exception {
        return Response.success(() -> sysDeptService.addPosts(list));
    }

    /**
     * 移除部门岗位
     *
     * @param deptId 部门id
     * @param postIds 岗位id列表
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:positions')")
    @DeleteMapping("/posts/{deptId}/{postIds}")
    public Response<Void> removePosts(@PathVariable Integer deptId, @PathVariable List<Integer> postIds) throws Exception {
        return Response.success(() -> sysDeptService.removePosts(deptId, postIds));
    }

    /**
     * 获取部门岗位（已设置）
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:query')")
    @GetMapping("/posts/configured")
    public Response<Response.Page<DeptPostDto>> getConfiguredPosts(@Valid DeptPostQuery query) {
        return Response.page(sysDeptService.getConfiguredPosts(query));
    }

    /**
     * 获取部门岗位（未设置）
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:query')")
    @GetMapping("/posts/unConfigured")
    public Response<Response.Page<DeptPostDto>> getUnConfiguredPosts(@Valid DeptPostQuery query) {
        return Response.page(sysDeptService.getUnConfiguredPosts(query));
    }

    /**
     * 添加部门成员
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:members')")
    @PostMapping("/members")
    public Response<Void> addMembers(@Valid @RequestBody List<SysUserDept> list) throws Exception {
        return Response.success(() -> sysDeptService.addMembers(list));
    }

    /**
     * 移除部门成员
     *
     * @param deptId 部门id
     * @param userIds 用户id列表
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:members')")
    @DeleteMapping("/members/{deptId}/{userIds}")
    public Response<Void> removeMembers(@PathVariable Integer deptId, @PathVariable List<Integer> userIds) throws Exception {
        return Response.success(() -> sysDeptService.removeMembers(deptId, userIds));
    }

    /**
     * 获取部门成员（已加入）
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:query')")
    @GetMapping("/members/joined")
    public Response<Response.Page<DeptUserDto>> getJoinedMembers(@Valid DeptUserQuery query) {
        return Response.page(sysDeptService.getJoinedMembers(query));
    }

    /**
     * 获取部门成员（未加入）
     */
    @PreAuthorize("@permit.hasPermit('sys:dept:query')")
    @GetMapping("/members/unJoined")
    public Response<Response.Page<DeptUserDto>> getUnJoinedMembers(@Valid DeptUserQuery query) {
        return Response.page(sysDeptService.getUnJoinedMembers(query));
    }

    /**
     * 部门流程候选人
     *
     * @param deptCode 部门编码
     */
    @GetMapping("/candidates/{deptCode}")
    public Response<List<UserNameDto>> getCandidatesByCode(@PathVariable String deptCode) {
        return Response.success(sysDeptService.getCandidatesByCode(deptCode));
    }
}
