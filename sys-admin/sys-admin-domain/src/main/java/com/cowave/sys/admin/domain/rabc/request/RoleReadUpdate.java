package com.cowave.sys.admin.domain.rabc.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author shanhuiming
 */
@Data
public class RoleReadUpdate {

    /**
     * 角色id
     */
    @NotNull(message = "{admin.role.id.notnull}")
    private Integer roleId;

    /**
     * 角色名称
     */
    @NotBlank(message = "{admin.role.name.notnull}")
    private String roleName;

    /**
     * 只读状态 TODO
     */
    @NotNull(message = "{role.notnull.readOnly}")
    private Integer readOnly;
}
