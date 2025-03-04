/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.app.base;

import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.domain.base.dto.SysAttachDto;
import com.cowave.sys.admin.domain.base.dto.SysNoticeDto;
import com.cowave.sys.admin.domain.base.dto.SysNoticeReadDto;
import com.cowave.sys.admin.service.base.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * 公告
 * @order 8
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notice")
public class SysNoticeController {

    private final SysNoticeService sysNoticeService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public Response<Response.Page<SysNoticeDto>> list(@RequestBody SysNoticeDto sysNotice) {
        return Response.page(sysNoticeService.list(sysNotice));
    }

    /**
     * 详情
     *
     * @param noticeId 公告ID
     */
    @GetMapping(value = "/info/{noticeId}")
    public Response<SysNoticeDto> info(@PathVariable Long noticeId) {
        return Response.success(sysNoticeService.info(noticeId));
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Response<Void> add(@Validated @RequestBody SysNoticeDto sysNotice) throws Exception {
        sysNoticeService.add(sysNotice);
        return Response.success();
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    public Response<String> edit(@Validated @RequestBody SysNoticeDto sysNotice) throws Exception {
        sysNoticeService.edit(sysNotice);
        return Response.success();
    }

    /**
     * 图片上传
     */
    @PostMapping("/image")
    public Response<SysAttachDto> imageUpload(@RequestParam("file") MultipartFile file, SysAttachDto image) throws Exception {
        return Response.success(sysNoticeService.imageUpload(file, image));
    }

    /**
     * 删除
     *
     * @param noticeId 公告ID
     */
    @GetMapping("/delete")
    public Response<Void> delete(@NotNull(message = "{notice.notnull.id}") Long[] noticeId) throws Exception {
        sysNoticeService.delete(noticeId);
        return Response.success();
    }

    /**
     * 发布
     *
     * @param noticeId 公告ID
     */
    @GetMapping(value = "/publish/{noticeId}")
    public Response<Void> publish(@PathVariable Long noticeId) {
        sysNoticeService.publish(noticeId);
        return Response.success();
    }

    /**
     * 已读列表
     *
     * @param noticeId 公告ID
     */
    @GetMapping(value = "/read/list")
    public Response<Response.Page<SysNoticeReadDto>> readList(@NotNull(message = "{notice.notnull.id}") Long noticeId) {
        return Response.page(sysNoticeService.readList(noticeId));
    }

    /**
     * 消息列表
     */
    @GetMapping("/msg/list")
    public Response<Response.Page<SysNoticeDto>> msgList() {
        return Response.page(sysNoticeService.msgList());
    }

    /**
     * 阅读消息
     *
     * @param noticeId 公告ID
     */
    @GetMapping(value = "/msg/read/{noticeId}")
    public Response<Void> msgRead(@PathVariable Long noticeId) {
        sysNoticeService.msgRead(noticeId);
        return Response.success();
    }

    /**
     * 反馈消息
     *
     * @param noticeId 公告ID
     */
    @GetMapping(value = "/msg/back")
    public Response<Void> msgBack(@NotNull(message = "{notice.notnull.id}") Long noticeId, String readBack) {
        sysNoticeService.msgBack(noticeId, readBack);
        return Response.success();
    }

    /**
     * 删除消息
     *
     * @param noticeId 公告ID
     */
    @GetMapping("/msg/delete")
    public Response<Void> msgDelete(@NotNull(message = "{notice.notnull.id}") Long noticeId) {
        sysNoticeService.msgDelete(noticeId);
        return Response.success();
    }

    /**
     * 未读统计
     */
    @GetMapping("/count/unread")
    public Response<Integer> countUnRead() {
        return Response.success(sysNoticeService.countUserUnRead(Access.userId()));
    }
}
