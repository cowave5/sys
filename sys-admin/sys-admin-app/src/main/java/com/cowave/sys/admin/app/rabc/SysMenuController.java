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
import com.cowave.commons.framework.support.excel.CellWidthHandler;
import com.cowave.sys.admin.domain.rabc.dto.SysMenuDto;
import com.cowave.sys.admin.domain.rabc.dto.SysRoleDto;
import com.cowave.sys.admin.domain.rabc.vo.RoleAuthed;
import com.cowave.sys.admin.service.rabc.SysMenuService;
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
 * 菜单
 * @order 5
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/menu")
public class SysMenuController{

	private final SysMenuService sysMenuService;

	/**
	 * 树
	 */
	@GetMapping("/tree")
	public Response<List<Tree<Integer>>> tree(){
		return Response.success(sysMenuService.tree());
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public Response<Response.Page<SysMenuDto>> list(String menuName, Integer menuStatus){
		return Response.page(sysMenuService.list(menuName, menuStatus, null, false));
	}

	/**
     * 详情
     *
     * @param menuId 菜单id
     */
    @GetMapping(value = "/info/{menuId}")
    public Response<SysMenuDto> info(@PathVariable Integer menuId) {
        return Response.success(sysMenuService.info(menuId));
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Response<Long> add(@Validated @RequestBody SysMenuDto sysMenu) {
    	sysMenuService.add(sysMenu);
        return Response.success();
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    public Response<Void> edit(@Validated @RequestBody SysMenuDto sysMenu) {
    	sysMenuService.edit(sysMenu);
        return Response.success();
    }

    /**
     * 删除
     *
     * @param menuId 菜单id
     */
    @GetMapping("/delete")
    public Response<Void> delete(@NotNull(message = "{menu.notnull.id}") Integer menuId) {
    	sysMenuService.delete(menuId);
        return Response.success();
    }

	/**
	 * 修改只读
	 */
	@PreAuthorize("@permit.hasPermit('sys:common:readonly')")
	@PostMapping("/change/readonly")
	public Response<Void> changeReadonly(@RequestBody SysMenuDto sysMenu) {
		sysMenuService.changeReadonly(sysMenu);
		return Response.success();
	}

	/**
	 * 导出
	 */
	@RequestMapping("/export")
	public void export(HttpServletResponse response) throws IOException {
		String fileName = URLEncoder.encode("菜单数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), SysMenuDto.class)
		.sheet("菜单").registerWriteHandler(new CellWidthHandler()).doWrite(sysMenuService.list(null, null, null, false));
	}

	/**
     * 已授权角色
     */
	@PostMapping("/role/authed")
    public Response<Response.Page<SysRoleDto>> roleAuthed(@Validated @RequestBody RoleAuthed roleAuthed) {
    	return Response.page(sysMenuService.roleAuthed(roleAuthed));
    }

    /**
     * 未授权角色
     */
	@PostMapping("/role/unauthed")
    public Response<Response.Page<SysRoleDto>> roleUnAuthed(@Validated @RequestBody RoleAuthed roleAuthed) {
    	return Response.page(sysMenuService.roleUnAuthed(roleAuthed));
    }

    /**
     * 授予角色菜单
     */
    @PostMapping("/role/grant")
    public Response<Void> grant(@Validated @RequestBody RoleAuthed roleAuthed) {
        sysMenuService.grant(roleAuthed);
    	return Response.success();
    }

    /**
     * 取消角色菜单
     */
    @PostMapping("/role/cancel")
    public Response<Void> cancel(@Validated @RequestBody RoleAuthed roleAuthed) {
        sysMenuService.cancel(roleAuthed);
    	return Response.success();
    }
}
