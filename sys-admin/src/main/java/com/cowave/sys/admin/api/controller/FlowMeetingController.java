/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.api.controller;

import com.cowave.sys.admin.api.entity.flow.Meeting;
import com.cowave.sys.admin.api.service.FlowMeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.web.bind.annotation.*;

/**
 * 会议预约
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/flow/meeting")
public class FlowMeetingController {

    private final FlowMeetingService flowMeetingService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public Response<Response.Page<Meeting>> list(@RequestBody Meeting meeting) {
        return Response.page(flowMeetingService.list(meeting));
    }

    /**
     * 删除
     */
    @GetMapping( "/info/{id}")
    public Response<Meeting> info(@PathVariable Long id) {
        return Response.success(flowMeetingService.info(id));
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Response<Void> add(@RequestBody Meeting meeting) {
        flowMeetingService.add(meeting);
        return Response.success();
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    public Response<Void> edit(@RequestBody Meeting meeting) {
        flowMeetingService.edit(meeting);
        return Response.success();
    }

    /**
     * 删除
     */
    @GetMapping( "/delete/{ids}")
    public Response<Void> delete(@PathVariable Long[] ids) {
        flowMeetingService.delete(ids);
        return Response.success();
    }
}
