/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.auth.enums;

import com.cowave.commons.tools.EnumVal;
import com.cowave.sys.admin.domain.auth.OAuthServer;
import lombok.RequiredArgsConstructor;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
public enum OAuthApp implements EnumVal<String> {

    /**
     * gitlab
     */
    GITLAB("gitlab");

    private final String val;

    @Override
    public String val() {
        return val;
    }

    /**
     * 重定向Url，获取授权码code
     */
    public String getAuthUrl(OAuthServer oauth) {
        return oauth.getAuthUrl() + "/oauth/authorize?client_id=" + oauth.getAppId()
                + "&redirect_uri=" + oauth.getRedirectUrl()
                + "&response_type=" + oauth.getResponseType();
    }

    /**
     * 使用授权码code，获取令牌
     */
    public String getTokenUrl(OAuthServer oauth, String code) {
        return oauth.getAuthUrl() + "/oauth/token?client_id=" + oauth.getAppId()
                + "&client_secret=" + oauth.getAppSecret()
                + "&redirect_uri=" + oauth.getRedirectUrl()
                + "&grant_type=" + oauth.getGrantType()
                + "&scope=" + oauth.getAuthScope()
                + "&code=" + code;
    }

    /**
     * 使用令牌，获取资源
     */
    public String getUserUrl(OAuthServer oauth){
        return oauth.getAuthUrl() + "/api/v4/user";
    }
}
