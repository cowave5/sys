/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.app.base;

import com.alibaba.excel.EasyExcel;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.support.excel.write.ExcelIgnoreStyle;
import com.cowave.sys.admin.domain.base.SysConfig;
import com.cowave.sys.admin.domain.base.request.ConfigQuery;
import com.cowave.sys.admin.service.base.SysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 系统配置
 * @order 14
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/config")
public class SysConfigController {

    private final SysConfigService sysConfigService;

    /**
     * 列表
     */
    @PreAuthorize("@permits.hasPermit('sys:config:query')")
    @GetMapping
    public Response<Response.Page<SysConfig>> list(ConfigQuery query) {
        return Response.page(sysConfigService.page(Access.tenantId(), query));
    }

    /**
     * 详情
     */
    @PreAuthorize("@permits.hasPermit('sys:config:query')")
    @GetMapping("/{configId}")
    public Response<SysConfig> info(@PathVariable Integer configId) {
        return Response.success(sysConfigService.info(Access.tenantId(), configId));
    }

    /**
     * 新增
     */
    @PreAuthorize("@permits.hasPermit('sys:config:create')")
    @PostMapping
    public Response<Void> create(@RequestBody SysConfig sysConfig) throws Exception {
        return Response.success(() -> sysConfigService.add(sysConfig));
    }

    /**
     * 删除
     */
    @PreAuthorize("@permits.hasPermit('sys:config:delete')")
    @DeleteMapping("/{configIds}")
    public Response<Void> delete(@PathVariable List<Integer> configIds) throws Exception {
        return Response.success(() -> sysConfigService.delete(Access.tenantId(), configIds));
    }

    /**
     * 修改
     */
    @PreAuthorize("@permits.hasPermit('sys:config:edit')")
    @PatchMapping
    public Response<Void> edit(@RequestBody SysConfig sysConfig) throws Exception {
        return Response.success(() -> sysConfigService.edit(sysConfig));
    }

    /**
     * 导出
     */
    @PreAuthorize("@permits.hasPermit('sys:config:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, ConfigQuery query) throws IOException {
        String fileName = URLEncoder.encode("系统参数", StandardCharsets.UTF_8).replace("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        List<SysConfig> configList = sysConfigService.list(Access.tenantId(), query);
        EasyExcel.write(response.getOutputStream(), SysConfig.class)
                .sheet("系统参数").registerWriteHandler(new ExcelIgnoreStyle()).doWrite(configList);
    }

    /**
     * 重置恢复
     */
    @PreAuthorize("@permits.hasPermit('sys:config:reset')")
    @GetMapping("/reset")
    public Response<Void> resetConfig() throws Exception {
        return Response.success(() -> sysConfigService.resetConfig(Access.tenantId()));
    }

    /**
     * 获取配置
     */
    @GetMapping("/value/{configKey}")
    public Response<Object> getConfigValue(@PathVariable String configKey) {
        Object configValue = sysConfigService.getConfigValue(Access.tenantId(), configKey);
        return Response.success(configValue);
    }
}
