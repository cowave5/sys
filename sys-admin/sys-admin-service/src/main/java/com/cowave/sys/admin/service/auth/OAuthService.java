/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.auth;

import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.sys.admin.domain.auth.OAuthConfig;
import com.cowave.sys.admin.domain.auth.OAuthUser;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface OAuthService {

    /**
     * 保存授权配置
     */
    void saveConfig(OAuthConfig oAuthConfig);

    /**
     * OAuth配置获取
     */
    OAuthConfig getConfig(String appType);

    /**
     * 获取授权配置
     */
    List<OAuthConfig> listConfig();

    /**
     * 获取授权用户
     */
    List<OAuthUser> listUser(OAuthUser oAuthUser);

    /**
     * 用户信息
     */
    OAuthUser infoUser(Integer id);

    /**
     * 修改用户角色
     */
    void changeUserRole(Integer userId, Integer roleId);

    /**
     * 删除用户
     */
    void deleteUser(Integer userId);

    /**
     * gitlab回调
     */
    AccessUserDetails gitlabCallback(String code);
}
