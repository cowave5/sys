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
import com.cowave.commons.framework.support.excel.CellWidthHandler;
import com.cowave.sys.admin.core.caches.SysConfigCaches;
import com.cowave.sys.admin.api.service.SysConfigService;
import com.cowave.sys.model.admin.SysConfig;
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

/**
 * 系统配置
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/config")
public class SysConfigController {

    private final SysConfigCaches sysConfigCaches;

    private final SysConfigService sysConfigService;

    /**
     * 刷新缓存
     */
    @PreAuthorize("@permit.hasPermit('sys:config:cache')")
    @GetMapping(value = "/refresh")
    public Response<Void> refresh() throws Exception {
        sysConfigCaches.refresh();
        return Response.success();
    }

    /**
     * 获取参数值
     */
    @GetMapping(value = "/value/{configKey}")
    public Response<String> getValue(@PathVariable @NotNull(message = "{config.notnull.key}") String configKey) {
        return Response.success(sysConfigCaches.getValue(configKey));
    }

    /**
     * 列表
     */
    @PreAuthorize("@permit.hasPermit('sys:config:query')")
    @PostMapping(value = "/list")
    public Response<Response.Page<SysConfig>> list(@RequestBody SysConfig sysConfig) {
        return Response.page(sysConfigService.list(sysConfig));
    }

    /**
     * 详情
     */
    @PreAuthorize("@permit.hasPermit('sys:config:query')")
    @GetMapping(value = "/info/{configId}")
    public Response<SysConfig> info(@PathVariable Integer configId) {
        return Response.success(sysConfigService.info(configId));
    }

    /**
     * 新增
     */
    @PreAuthorize("@permit.hasPermit('sys:config:new')")
    @PostMapping(value = "/add")
    public Response<Void> add(@RequestBody SysConfig sysConfig) throws Exception {
        sysConfigService.add(sysConfig);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PreAuthorize("@permit.hasPermit('sys:config:edit')")
    @PostMapping(value = "/edit")
    public Response<Void> edit(@RequestBody SysConfig sysConfig) throws Exception {
        sysConfigService.edit(sysConfig);
        return Response.success();
    }

    /**
     * 删除
     */
    @PreAuthorize("@permit.hasPermit('sys:config:delete')")
    @GetMapping(value = "/delete")
    public Response<Void> delete(@NotNull(message = "{config.notnull.id}") Integer[] configId) throws Exception {
        sysConfigService.delete(configId);
        return Response.success();
    }

    /**
     * 导出
     */
    @PreAuthorize("@permit.hasPermit('sys:config:export')")
    @RequestMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        String fileName = URLEncoder.encode("系统参数", StandardCharsets.UTF_8).replace("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        EasyExcel.write(response.getOutputStream(), SysConfig.class)
                .sheet("系统参数").registerWriteHandler(new CellWidthHandler()).doWrite(sysConfigService.list(new SysConfig()).getRecords());
    }
}
