/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.auth;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.sys.admin.domain.auth.OAuthClient;
import com.cowave.sys.admin.domain.auth.OAuthServer;
import com.cowave.sys.admin.domain.auth.dto.OAuthUserDto;
import com.cowave.sys.admin.domain.auth.request.OAuth2CodeRequest;
import com.cowave.sys.admin.domain.auth.request.OAuth2TokenRequest;
import com.cowave.sys.admin.domain.auth.request.OAuthUserQuery;
import com.cowave.sys.admin.domain.auth.vo.OAuth2CodeVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author shanhuiming
 */
public interface OAuthService {

    /**
     * gitlab回调
     */
    AccessUserDetails gitlabCallback(String tenantId, String code);

    /**
     * 获取授权服务配置
     */
    OAuthServer getServerConfig(String tenantId, String serverType);

    /**
     * 修改授权服务配置
     */
    void updateServerConfig(String tenantId, OAuthServer oAuthServer);

    /**
     * 用户列表
     */
    List<OAuthUserDto> listUser(String tenantId, OAuthUserQuery query);

    /**
     * 删除用户
     */
    void deleteUser(String tenantId, Integer userId);

    /**
     * 修改用户角色
     */
    void updateUserRole(String tenantId, Integer userId, String roleCode);

    /**
     * 授权客户端列表
     */
    Page<OAuthClient> listClient(String tenantId, String clientName);

    /**
     * 新增客户端列表
     */
    OAuthClient createClient(String tenantId, OAuthClient oAuthClient);

    /**
     * 删除客户端列表
     */
    void deleteClient(String tenantId, List<Integer> ids);

    /**
     * 客户端获取授权码
     */
    OAuth2CodeVo getClientCode(OAuth2CodeRequest request);

    /**
     * 客户端地址回调
     */
    void clientRedirect(String code, HttpServletResponse response) throws IOException;

    /**
     * 客户端获取令牌
     */
    AccessUserDetails getClientToken(OAuth2TokenRequest request);
}
