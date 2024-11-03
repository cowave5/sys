package com.cowave.sys.admin.core.entity.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author shanhuiming
 */
@Data
public class UserReadUpdate {

    /**
     * 用户id
     */
    @NotNull(message = "{user.notnull.id}")
    private Long userId;

    /**
     * 用户名称
     */
    @NotBlank(message = "{user.notnull.name}")
    private String userName;

    /**
     * 用户状态
     */
    @NotNull(message = "{user.notnull.readOnly}")
    private Integer readOnly;
}
