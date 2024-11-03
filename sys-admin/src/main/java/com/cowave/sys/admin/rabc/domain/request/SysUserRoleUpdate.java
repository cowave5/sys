package com.cowave.sys.admin.rabc.domain.request;

import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.rabc.domain.SysUserRole;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author shanhuiming
 */
@Data
public class SysUserRoleUpdate {

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
     * 角色id列表
     */
    private List<Integer> roleIds;

    public List<SysUserRole> getUserRoles(){
        return Collections.copyToList(roleIds, v -> new SysUserRole(userId, v));
    }
}
