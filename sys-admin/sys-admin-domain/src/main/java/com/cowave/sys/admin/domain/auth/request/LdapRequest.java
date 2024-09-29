package com.cowave.sys.admin.domain.auth.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author shanhuiming
 */
@NoArgsConstructor
@Data
public class LdapRequest {

    /**
     * 用户名
     */
	@NotBlank(message = "{admin.user.account.notnull}")
    private String userAccount;

    /**
     * 用户密码
     */
    @NotBlank(message = "{admin.user.passwd.notnull}")
    private String passWord;
}
