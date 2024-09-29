package com.cowave.sys.admin.domain.auth.request;

import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class OAuthUserQuery {

    /**
     * 应用类型
     */
    private String appType;

    /**
     * 用户账号
     */
    private String userAccount;
}
