package com.cowave.sys.admin.rabc.infra.constants;

import com.cowave.commons.tools.EnumVal;
import lombok.RequiredArgsConstructor;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
public enum SysUserType implements EnumVal<Integer> {

    /**
     * 系统用户
     */
    SYS(0, "sys"),

    /**
     * Ldap用户
     */
    LDAP(1, "ldap");

    private final Integer val;

    private final String desc;

    @Override
    public Integer val() {
        return val;
    }

    @Override
    public String desc() {
        return desc;
    }
}
