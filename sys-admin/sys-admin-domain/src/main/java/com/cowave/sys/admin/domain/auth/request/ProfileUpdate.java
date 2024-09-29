package com.cowave.sys.admin.domain.auth.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author shanhuiming
 */
@Data
public class ProfileUpdate {

    /**
     * 用户名称
     */
    @NotBlank(message = "{admin.user.name.notnull}")
    private String userName;

    /**
     * 用户性别
     */
    private Integer userSex;

    /**
     * 用户电话
     */
    @Pattern(regexp = "^[1][3,4,5,7,8][0-9]{9}$", message = "{admin.user.phone.invalid}")
    private String userPhone;

    /**
     * 用户邮箱
     */
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{admin.user.email.invalid}")
    private String userEmail;
}
