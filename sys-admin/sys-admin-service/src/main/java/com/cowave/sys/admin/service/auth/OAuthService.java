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
import com.cowave.sys.admin.domain.auth.dto.OAuthUserDto;
import com.cowave.sys.admin.domain.auth.request.OAuthUserQuery;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface OAuthService {

    /**
     * gitlab回调
     */
    AccessUserDetails gitlabCallback(String code);

    /**
     * 启用的配置集合
     */
    List<OAuthConfig> queryEnabledList();

    /**
     * 获取配置
     */
    OAuthConfig queryByAppType(String appType);

    /**
     * 修改配置
     */
    void updateConfig(OAuthConfig oAuthConfig);

    /**
     * 用户列表
     */
    List<OAuthUserDto> listUser(OAuthUserQuery query);

    /**
     * 用户信息
     */
    OAuthUser infoUser(Integer id);

    /**
     * 删除用户
     */
    void deleteUser(Integer userId);

    /**
     * 修改用户角色
     */
    void changeUserRole(Integer userId, String roleCode);
}
