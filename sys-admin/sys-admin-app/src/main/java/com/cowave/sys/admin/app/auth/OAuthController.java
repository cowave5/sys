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
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.sys.admin.domain.auth.OAuthConfig;
import com.cowave.sys.admin.domain.auth.OAuthUser;
import com.cowave.sys.admin.service.auth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * OAuth授权
 * @order 24
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth")
public class OAuthController {

    private final OAuthService oauthService;

    /**
     * 保存授权配置
     */
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:edit')")
    @PostMapping("/config/save")
    public Response<Void> saveConfig(@RequestBody OAuthConfig oAuthConfig) {
        oauthService.saveConfig(oAuthConfig);
        return Response.success();
    }

    /**
     * 获取授权配置
     */
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:query')")
    @GetMapping("/config/get/{appType}")
    public Response<OAuthConfig> getConfig(@PathVariable String appType) {
        return Response.success(oauthService.getConfig(appType));
    }

    /**
     * 获取授权用户
     */
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:user:query')")
    @PostMapping("/user/list")
    public Response<Response.Page<OAuthUser>> userList(@RequestBody OAuthUser oAuthUser) {
        return Response.page(oauthService.listUser(oAuthUser));
    }

    /**
     * 修改用户角色
     *
     * @param userId 用户id
     * @param roleId 角色id
     */
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:user:edit')")
    @GetMapping("/user/role/change")
    public Response<Void> changeUserRole(
            @NotNull(message = "{user.notnull.id}") Integer userId, @NotNull(message = "{role.notnull.id}") Integer roleId) {
        oauthService.changeUserRole(userId, roleId);
        return Response.success();
    }

    /**
     * 删除用户
     *
     * @param userId 用户id
     */
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:user:delete')")
    @GetMapping("/user/delete")
    public Response<Void> deleteUser(@NotNull(message = "{user.notnull.id}") Integer userId) {
        oauthService.deleteUser(userId);
        return Response.success();
    }

    /**
     * gitlab回调
     */
    @GetMapping("/callback/gitlab")
    public Response<AccessUserDetails> gitlabCallback(@RequestParam("code") String code) {
        return Response.success(oauthService.gitlabCallback(code));
    }
}
