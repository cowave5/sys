/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.quartz.api.task.controller;

import com.alibaba.excel.EasyExcel;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.support.excel.CellWidthHandler;
import com.cowave.sys.quartz.api.task.entity.QuartzLog;
import com.cowave.sys.quartz.api.task.service.TaskLogService;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 任务日志
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/task/log")
public class TaskLogController {

    private final TaskLogService taskLogService;

    /**
     * 列表
     */
    @PreAuthorize("@permit.hasPermit('monitor:quartz:log:query')")
    @PostMapping("/list")
    public Response<Response.Page<QuartzLog>> list(@RequestBody QuartzLog quartzLog) {
        return Response.page(taskLogService.list(quartzLog));
    }

    /**
     * 删除
     */
    @PreAuthorize("@permit.hasPermit('monitor:quartz:log:delete')")
    @GetMapping("/delete")
    public Response<Void> remove(Long[] id) {
        taskLogService.delete(id);
        return Response.success();
    }

    /**
     * 清空
     */
    @PreAuthorize("@permit.hasPermit('monitor:quartz:log:delete')")
    @GetMapping("/clean")
    public Response<Void> clean() {
        taskLogService.clean();
        return Response.success();
    }

    /**
     * 导出
     */
    @PreAuthorize("@permit.hasPermit('monitor:quartz:log:export')")
    @RequestMapping("/export")
    public void export(HttpServletResponse response, QuartzLog quartzLog) throws IOException {
        String fileName = URLEncoder.encode("任务日志", StandardCharsets.UTF_8).replace("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        EasyExcel.write(response.getOutputStream(), QuartzLog.class);
        PageHelper.clearPage();
        List<QuartzLog> list = taskLogService.list(quartzLog);
        EasyExcel.write(response.getOutputStream(), QuartzLog.class)
                .sheet("日志").registerWriteHandler(new CellWidthHandler()).doWrite(list);
    }
}
