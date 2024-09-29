package com.cowave.sys.admin.domain.rabc.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shanhuiming
 */
@Data
public class RoleMenuUpdate {

    /**
     * 角色id
     */
    @NotNull(message = "{admin.role.id.notnull}")
    private Integer roleId;

    /**
	 * 菜单id列表
	 */
    @NotNull(message = "{admin.menu.ids.notnull}")
	private List<Integer> menuIds = new ArrayList<>();
}
