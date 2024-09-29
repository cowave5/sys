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
import com.cowave.sys.admin.domain.auth.OAuthConfig;
import com.cowave.sys.admin.domain.auth.dto.OAuthUserDto;
import com.cowave.sys.admin.domain.auth.request.OAuthUserQuery;
import com.cowave.sys.admin.service.auth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * OAuth配置
 * @order 25
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth")
public class OAuthController {

    private final OAuthService oauthService;

    /**
     * 获取配置
     */
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:query')")
    @GetMapping("/config/{appType}")
    public Response<OAuthConfig> getConfig(@PathVariable String appType) {
        return Response.success(oauthService.queryByAppType(appType));
    }

    /**
     * 修改配置
     */
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:edit')")
    @PatchMapping("/config")
    public Response<Void> updateConfig(@RequestBody OAuthConfig oAuthConfig) throws Exception {
        return Response.success(() -> oauthService.updateConfig(oAuthConfig));
    }

    /**
     * 用户列表
     */
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:user:query')")
    @GetMapping("/user")
    public Response<Response.Page<OAuthUserDto>> listUser(OAuthUserQuery query) {
        return Response.page(oauthService.listUser(query));
    }

    /**
     * 删除用户
     *
     * @param userId 用户id
     */
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:user:delete')")
    @DeleteMapping("/user/{userId}")
    public Response<Void> deleteUser(@PathVariable Integer userId) throws Exception {
        return Response.success(() -> oauthService.deleteUser(userId));
    }

    /**
     * 修改用户角色
     *
     * @param userId 用户id
     * @param roleCode 角色编码
     */
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:user:edit')")
    @PatchMapping("/user/role/{userId}/{roleCode}")
    public Response<Void> changeUserRole(@PathVariable Integer userId, @PathVariable String roleCode) throws Exception {
        return Response.success(() -> oauthService.changeUserRole(userId, roleCode));
    }
}
