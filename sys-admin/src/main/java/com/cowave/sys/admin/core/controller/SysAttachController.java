/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.controller;

import com.cowave.commons.response.Response;
import com.cowave.sys.admin.core.service.SysAttachService;
import com.cowave.sys.admin.core.entity.dto.SysAttachDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 附件
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/attach")
public class SysAttachController {

    private final SysAttachService sysAttachService;

    /**
     * 上传
     */
    @PostMapping("/upload")
    public Response<SysAttachDto> upload(MultipartFile file, SysAttachDto sysAttach) throws Exception {
        return Response.success(sysAttachService.upload(file, sysAttach, false));
    }

    /**
     * 下载
     *
     * @param attachId 附件id
     */
    @GetMapping("/download")
    public void download(HttpServletResponse response, @NotNull(message = "{attach.notnull.id}") Long attachId) throws Exception {
        sysAttachService.download(response, attachId);
    }

    /**
     * 预览
     *
     * @param attachId 附件id
     */
    @GetMapping("/preview")
    public Response<String> preview(@NotNull(message = "{attach.notnull.id}") Long attachId) throws Exception {
        return Response.success(sysAttachService.preview(attachId));
    }

    /**
     * 列表
     *
     * @param masterId 宿主id
     * @param attachGroup 附件分组
     * @param attachType 附件类型
     */
    @GetMapping("/list")
    public Response<List<SysAttachDto>> list(
            @NotNull(message = "{attach.notnull.master}") Long masterId,
            @NotNull(message = "{attach.notnull.group}") String attachGroup,
            @NotNull(message = "{attach.notnull.type}") String attachType) throws Exception {
        return Response.success(sysAttachService.list(masterId, attachGroup, attachType));
    }

    /**
     * 删除
     *
     * @param attachId 附件id
     */
    @GetMapping("/delete")
    public Response<Void> delete(@NotNull(message = "{attach.notnull.id}") Long attachId) throws Exception {
        sysAttachService.delete(attachId);
        return Response.success();
    }
}
