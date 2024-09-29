/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.app.auth;

import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.admin.domain.auth.dto.UserProfile;
import com.cowave.sys.admin.domain.auth.request.PasswdReset;
import com.cowave.sys.admin.domain.auth.request.ProfileUpdate;
import com.cowave.sys.admin.domain.base.SysAttach;
import com.cowave.sys.admin.domain.base.request.AttachUpload;
import com.cowave.sys.admin.service.auth.ProfileService;
import com.cowave.sys.admin.service.base.SysAttachService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 个人信息
 * @order 9
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final SysAttachService sysAttachService;

    private final ProfileService profileService;

    /**
     * 详情
     */
    @GetMapping(value = {"/info"})
    public Response<UserProfile> info() {
        return Response.success(profileService.info());
    }

    /**
     * 修改
     */
    @PostMapping(value = {"/edit"})
    public Response<Void> edit(@Validated @RequestBody ProfileUpdate profile) throws Exception {
        return Response.success(() -> profileService.edit(profile));
    }

    /**
     * 重置密码
     */
    @PostMapping(value = {"/passwd/reset"})
    public Response<Void> resetPasswd(@RequestBody PasswdReset passwdReset) throws Exception {
        return Response.success(() -> profileService.resetPasswd(passwdReset));
    }

    /**
     * 头像上传
     */
    @PostMapping("/avatar")
    public Response<String> avatar(@RequestParam("file") MultipartFile file, AttachUpload attachUpload) throws Exception {
        SysAttach attach = sysAttachService.upload(file, attachUpload);
        sysAttachService.masterReserve(attach.getMasterId(), attach.getAttachGroup(), attach.getAttachType(), 3);
        return Response.success(sysAttachService.preview(attach));
    }
}
