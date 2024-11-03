/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.oauth.domain;

import com.cowave.commons.framework.access.security.AccessInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class OAuthConfig extends AccessInfo {

    /**
     * Gitlab
     */
    public static final String APP_GITLAB = "gitlab";

    /**
     * Gitlab auth api
     */
    public static final String GITLAB_AUTH = "/oauth/authorize";

    /**
     * Gitlab token api
     */
    public static final String GITLAB_TOKEN = "/oauth/token";

    /**
     * Gitlab user api
     */
    public static final String GITLAB_USER = "/api/v4/user";

    /**
     * id
     */
    private Integer id;

    /**
     * 状态
     */
    private int status;

    /**
     * 用户角色
     */
    private Integer userRole;

    /**
     * 应用类型
     */
    private String appType;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 应用secret
     */
    private String appSecret;

    /**
     * 授权服务url
     */
    private String authUrl;

    /**
     * 应用回调地址
     */
    private String redirectUrl;

    /**
     * 授权方式
     */
    private String grantType = "authorization_code";

    /**
     * 授权范围
     */
    private String authScope = "read_user";

    /**
     * Token响应类型
     */
    private String responseType = "code";

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建部门
     */
    private Integer createDept;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private Integer updateUser;

    /**
     * 更新部门
     */
    private Integer updateDept;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public String oauthUrl(){
        if(APP_GITLAB.equals(appType)){
            return gitlabAuthUrl();
        }
        return null;
    }

    /**
     * 重定向Url，获取授权码code
     */
    public String gitlabAuthUrl(){
        return authUrl + GITLAB_AUTH + "?client_id=" + appId + "&redirect_uri=" + redirectUrl + "&response_type=" + responseType;
    }

    /**
     * 使用授权码code获取令牌
     */
    public String gitlabTokenUrl(String code){
        return authUrl + GITLAB_TOKEN + "?client_id=" + appId + "&client_secret=" + appSecret
                + "&redirect_uri=" + redirectUrl + "&grant_type=" + grantType + "&scope=" + authScope + "&code=" + code;
    }

    /**
     * 使用令牌获取资源
     */
    public String gitlabUserUrl(){
        return authUrl + GITLAB_USER;
    }
}
