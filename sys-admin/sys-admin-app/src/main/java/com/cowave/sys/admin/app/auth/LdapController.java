/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.app.auth;

import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.domain.auth.LdapConfig;
import com.cowave.sys.admin.domain.auth.LdapUser;
import com.cowave.sys.admin.service.auth.LdapService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Ldap鉴权
 * @order 10
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ldap")
public class LdapController {

    private final LdapService ldapService;

    /**
     * 获取配置
     */
    @PreAuthorize("@permits.hasPermit('sys:ldap:query')")
    @GetMapping("/config")
    public Response<LdapConfig> getConfig() {
        return Response.success(ldapService.getConfig(Access.tenantId()));
    }

    /**
     * 修改配置
     */
    @PreAuthorize("@permits.hasPermit('sys:ldap:edit')")
    @PatchMapping("/config")
    public Response<Void> updateConfig(@Validated @RequestBody LdapConfig ldapConfig) throws Exception {
        return Response.success(() -> ldapService.updateConfig(Access.tenantId(), ldapConfig));
    }

    /**
     * 测试配置
     */
    @PreAuthorize("@permits.hasPermit('sys:ldap:edit')")
    @PostMapping("/config/valid")
    public Response<Void> validConfig(@Validated @RequestBody LdapConfig ldapConfig) throws Exception {
        return Response.success(() -> ldapService.validConfig(ldapConfig));
    }

    /**
     * 用户列表
     * @param ldapAccount ladp账号
     */
    @PreAuthorize("@permits.hasPermit('sys:ldap:query')")
    @GetMapping(value = {"/user"})
    public Response<Response.Page<LdapUser>> listUser(String ldapAccount) {
        return Response.page(ldapService.listUser(Access.tenantId(), ldapAccount));
    }
}
