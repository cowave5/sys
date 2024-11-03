package com.cowave.sys.admin.rabc.domain.request;

import com.cowave.commons.framework.access.security.AccessInfo;
import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.rabc.domain.SysUser;
import com.cowave.sys.admin.rabc.domain.SysUserDept;
import com.cowave.sys.admin.rabc.domain.SysUserRole;
import com.cowave.sys.admin.rabc.domain.SysUserTree;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserCreate extends SysUser {

    /**
	 * 角色id列表
	 */
	private List<Integer> roleIds;

    /**
	 * 上级用户id列表
	 */
	private List<Integer> parentIds;

    /**
	 * 部门-岗位id列表
	 */
	private List<String> deptPostIds;

    public void setCreateInfo(AccessInfo accessInfo){
        this.setCreateUser(accessInfo.getAccessUserId());
        this.setCreateDept(accessInfo.getAccessDeptId());
        this.setCreateTime(accessInfo.getAccessTime());
        this.setUpdateUser(accessInfo.getAccessUserId());
        this.setUpdateDept(accessInfo.getAccessDeptId());
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
				return new SysUserDept(getUserId(), Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
			});
    }
}
