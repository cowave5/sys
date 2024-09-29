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
    @NotNull(message = "{role.notnull.id}")
    private Integer roleId;

    /**
     * 用户id列表
     */
    @NotEmpty(message = "{role.notnull.userIds}")
    private List<Integer> userIds;
}
