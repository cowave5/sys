package com.cowave.sys.admin.core.entity.request;

import com.cowave.commons.framework.access.security.AccessInfo;
import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.core.entity.SysUser;
import com.cowave.sys.admin.core.entity.SysUserDept;
import com.cowave.sys.admin.core.entity.SysUserRole;
import com.cowave.sys.admin.core.entity.SysUserTree;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Optional;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserCreate extends SysUser {

    /**
	 * 角色id列表
	 */
	private List<Long> roleIds;

    /**
	 * 上级用户id列表
	 */
	private List<Long> parentIds;

    /**
	 * 部门-岗位id列表
	 */
	private List<String> deptPostIds;

    public void setCreateInfo(AccessInfo accessInfo){
        Long userId = Optional.ofNullable(accessInfo.getAccessUserId())
                .map(v -> ((Number)v).longValue()).orElse(null);
        Long deptId = Optional.ofNullable(accessInfo.getAccessDeptId())
                .map(v -> ((Number)v).longValue()).orElse(null);
        this.setCreateUser(userId);
        this.setCreateDept(deptId);
        this.setCreateTime(accessInfo.getAccessTime());
        this.setUpdateUser(userId);
        this.setUpdateDept(deptId);
        this.setUpdateTime(accessInfo.getAccessTime());
    }

    public List<SysUserRole> getUserRoles(){
        return Collections.copyToList(roleIds, v -> new SysUserRole(getUserId(), v));
    }

    public List<SysUserTree> getUserParents(){
        return Collections.copyToList(parentIds, v -> new SysUserTree(getUserId(), v));
    }

    public List<SysUserDept> getUserDeptPosts(){
        return Collections.copyToList(deptPostIds, v -> {
				String[] arr = v.split("-");
				return new SysUserDept(getUserId(), Long.parseLong(arr[0]), Long.parseLong(arr[1]));
			});
    }
}
