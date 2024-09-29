package com.cowave.sys.admin.domain.rabc.enums;

import com.cowave.commons.tools.EnumVal;
import lombok.RequiredArgsConstructor;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
public enum UserType implements EnumVal<String> {

    /**
     * 系统用户
     */
    SYS("sys"),

    /**
     * Ldap用户
     */
    LDAP("ldap"),

    /**
     * Gitlab用户
     */
    OAUTH_GITLAB("gitlab");

    private final String val;

    @Override
    public String val() {
        return val;
    }

    public String generateCode() {
        return val + "-" + java.util.UUID.randomUUID();
    }

    public boolean isTypeEquals(String userCode){
        return userCode.startsWith(val);
    }
}
