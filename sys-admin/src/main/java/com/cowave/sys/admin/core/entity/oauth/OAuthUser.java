/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.entity.oauth;

import com.cowave.commons.framework.access.security.AccessToken;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 授权用户
 */
@Data
public class OAuthUser {

    /**
     * id
     */
    private Long id;

    /**
     * 应用类型
     */
    private String appType;

    /**
     * 用户角色
     */
    private Integer userRole;

    /**
     * 用户角色名称
     */
    private String roleName;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户部门
     */
    private String userDept;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime = new Date();

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime = new Date();

    public static AccessToken accessToken(OAuthUser oauthUser){
        AccessToken accessToken = AccessToken.newToken();
        accessToken.setType(AccessToken.TYPE_OAUTH);
        accessToken.setUserId(oauthUser.id);
        accessToken.setUsername(oauthUser.userAccount);
        accessToken.setUserNick(oauthUser.userName);
        accessToken.setDeptName(oauthUser.userDept);
        return accessToken;
    }
}