package com.cowave.sys.admin.domain.rabc.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author shanhuiming
 */
@Data
public class UserPasswdUpdate {

     /**
     * 用户id
     */
    @NotNull(message = "{user.notnull.id}")
    private Integer userId;

    /**
     * 用户名称
     */
    @NotBlank(message = "{user.notnull.name}")
    private String userName;

    /**
     * 用户密码
     */
    @NotBlank(message = "{user.notnull.passwd}")
    private String userPasswd;
}
