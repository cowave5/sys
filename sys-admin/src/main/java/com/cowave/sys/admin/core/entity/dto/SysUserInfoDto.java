package com.cowave.sys.admin.core.entity.dto;

import com.cowave.sys.admin.core.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserInfoDto extends SysUser {

    /**
     * 角色列表
     */
    private List<Long> roleIds;

    /**
     * 上级用户列表
     */
    private List<Long> parentIds;

    /**
     * 部门岗位列表
     */
    private List<String> deptPostIds;
}
