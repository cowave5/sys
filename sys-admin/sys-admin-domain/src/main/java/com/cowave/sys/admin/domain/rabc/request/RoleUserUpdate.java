package com.cowave.sys.admin.domain.rabc.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author shanhuiming
 */
@Data
public class RoleUserUpdate {

    /**
     * 角色id
     */
    @NotNull(message = "{admin.role.id.notnull}")
    private Integer roleId;

    /**
     * 用户id列表
     */
    @NotEmpty(message = "{admin.user.ids.notnull}")
    private List<Integer> userIds;
}
