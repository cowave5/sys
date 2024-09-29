package com.cowave.sys.admin.domain.auth.dto;

import com.cowave.sys.admin.domain.auth.LdapUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LdapUserDto extends LdapUser {

    /**
     * 用户角色名称
     */
    private String roleName;
}
