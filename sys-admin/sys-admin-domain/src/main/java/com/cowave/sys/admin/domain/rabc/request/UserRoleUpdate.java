package com.cowave.sys.admin.domain.rabc.request;

import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.domain.rabc.SysUserRole;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author shanhuiming
 */
@Data
public class UserRoleUpdate {

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
     * 角色id列表
     */
    @NotBlank(message = "{admin.role.ids.notnull}")
    private List<Integer> roleIds;

    public List<SysUserRole> getUserRoles(){
        return Collections.copyToList(roleIds, v -> new SysUserRole(userId, v));
    }
}
