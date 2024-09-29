package com.cowave.sys.admin.domain.rabc.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shanhuiming
 */
@Data
public class RoleUserQuery {

    /**
     * 部门id
     */
    @NotNull(message = "{role.notnull.id}")
    private Integer roleId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户电话
     */
    private String userPhone;
}
