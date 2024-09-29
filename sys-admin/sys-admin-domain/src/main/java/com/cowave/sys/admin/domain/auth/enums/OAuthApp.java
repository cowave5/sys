package com.cowave.sys.admin.domain.auth.enums;

import com.cowave.commons.tools.EnumVal;
import com.cowave.sys.admin.domain.auth.OAuthConfig;
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
    public String getAuthUrl(OAuthConfig oauth) {
        return oauth.getAuthUrl() + "/oauth/authorize?client_id=" + oauth.getAppId()
                + "&redirect_uri=" + oauth.getRedirectUrl()
                + "&response_type=" + oauth.getResponseType();
    }

    /**
     * 使用授权码code，获取令牌
     */
    public String getTokenUrl(OAuthConfig oauth, String code) {
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
    public String getUserUrl(OAuthConfig oauth){
        return oauth.getAuthUrl() + "/api/v4/user";
    }
}
