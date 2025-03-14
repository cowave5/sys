package com.cowave.sys.admin.domain.rabc.request;

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
    @NotNull(message = "{admin.user.id.notnull}")
    private Integer userId;

    /**
     * 用户名称
     */
    @NotBlank(message = "{admin.user.name.notnull}")
    private String userName;

    /**
     * 只读状态
     */
    @NotNull(message = "{user.notnull.readOnly}")
    private Integer readOnly;
}
