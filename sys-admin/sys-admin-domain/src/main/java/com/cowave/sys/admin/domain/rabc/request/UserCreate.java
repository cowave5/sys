package com.cowave.sys.admin.domain.rabc.request;

import com.cowave.commons.framework.access.security.AccessInfoSetter;
import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.domain.rabc.SysUser;
import com.cowave.sys.admin.domain.rabc.SysUserDept;
import com.cowave.sys.admin.domain.rabc.SysUserRole;
import com.cowave.sys.admin.domain.rabc.SysUserTree;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserCreate extends SysUser implements AccessInfoSetter {

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
