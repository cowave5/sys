package com.cowave.sys.admin.domain.auth.dto;

import com.cowave.sys.admin.domain.auth.OAuthUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OAuthUserDto extends OAuthUser {

    /**
     * 用户角色名称
     */
    private String roleName;
}
