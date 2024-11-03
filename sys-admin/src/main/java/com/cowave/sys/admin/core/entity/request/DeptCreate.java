package com.cowave.sys.admin.core.entity.request;

import com.cowave.commons.framework.access.security.AccessInfo;
import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.core.entity.SysDept;
import com.cowave.sys.admin.core.entity.SysDeptTree;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeptCreate extends SysDept {

    /**
	 * 上级部门Id列表
	 */
	@NotEmpty(message = "{dept.notnull.prentIds}")
	private List<Integer> parentIds;

	public void setCreateInfo(AccessInfo accessInfo){
        this.setCreateUser(accessInfo.getAccessUserId());
        this.setCreateDept(accessInfo.getAccessDeptId());
        this.setCreateTime(accessInfo.getAccessTime());
        this.setUpdateUser(accessInfo.getAccessUserId());
        this.setUpdateDept(accessInfo.getAccessDeptId());
        this.setUpdateTime(accessInfo.getAccessTime());
    }

    public List<SysDeptTree> getDeptParents(){
        return Collections.copyToList(parentIds, v -> new SysDeptTree(getDeptId(), v));
    }
}
