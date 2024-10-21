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

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import com.cowave.sys.admin.api.service.SysAlarmService;
import com.cowave.sys.model.admin.SysAlarm;
import com.cowave.sys.model.admin.SysAlarmType;
import org.springframework.feign.codec.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.excel.EasyExcel;
import com.cowave.sys.admin.core.entity.AlarmHandles;
import com.cowave.commons.framework.support.excel.CellWidthHandler;

import lombok.RequiredArgsConstructor;

/**
 * 系统告警
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/alarm")
public class SysAlarmController {

	private final SysAlarmService sysAlarmService;

	/**
     * 类型列表
     */
	@PostMapping(value = "/type/list")
    public Response<Response.Page<SysAlarmType>> typeList(@RequestBody SysAlarmType sysAlarmType) {
        return Response.page(sysAlarmService.typeList(sysAlarmType));
    }

	/**
     * 类型新增
     */
	@PostMapping(value = "/type/add")
    public Response<Void> typeAdd(@Validated @RequestBody SysAlarmType sysAlarmType) {
		sysAlarmService.typeAdd(sysAlarmType);
        return Response.success();
    }

    /**
     * 类型修改
     */
	@PostMapping(value = "/type/edit")
    public Response<Void> typeEdit(@Validated @RequestBody SysAlarmType sysAlarmType) {
		sysAlarmService.typeEdit(sysAlarmType);
        return Response.success();
    }

    /**
     * 类型删除
     */
	@GetMapping(value = "/type/delete")
    public Response<Void> typeDelete(Long id) {
		sysAlarmService.typeDelete(id);
        return Response.success();
    }

	/**
	 * 列表
	 */
	@PostMapping("/list")
    public Response<Response.Page<SysAlarm>> list(@RequestBody SysAlarm sysAlarm) {
        return Response.page(sysAlarmService.list(sysAlarm));
    }

	/**
	 * 详情
	 *
	 * @param id 告警id
	 */
	@GetMapping(value = "/info")
    public Response<SysAlarm> info(@NotNull(message = "{valid.notnull.alarm.id}") Long id) {
        return Response.success(sysAlarmService.info(id));
    }

	/**
	 * 删除
	 *
	 * @param id 告警id
	 */
	@GetMapping("/delete")
	public Response<Void> delete(@NotNull(message = "{valid.notnull.alarm.id}") Long id) {
		sysAlarmService.delete(id);
		return Response.success();
	}

	/**
	 * 导出
	 */
	@PostMapping("/export")
	public void export(HttpServletResponse response, SysAlarm sysAlarm) throws IOException {
		String fileName = URLEncoder.encode("系统告警", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), SysAlarm.class)
		.sheet("系统告警").registerWriteHandler(new CellWidthHandler()).doWrite(sysAlarmService.list(sysAlarm).getRecords());
	}

	/**
     * 告警处理
     */
    @PostMapping("/handle")
    public Response<Void> handle(@Validated @RequestBody AlarmHandles alarmHandles) {
        sysAlarmService.handle(alarmHandles);
        return Response.success();
    }
}
