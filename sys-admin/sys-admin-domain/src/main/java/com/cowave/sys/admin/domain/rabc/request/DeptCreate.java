package com.cowave.sys.admin.domain.rabc.request;

import com.cowave.commons.framework.access.security.AccessInfoSetter;
import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.domain.rabc.SysDept;
import com.cowave.sys.admin.domain.rabc.SysDeptTree;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeptCreate extends SysDept implements AccessInfoSetter {

    /**
	 * 上级部门Id列表
	 */
	@NotEmpty(message = "{admin.dept.parentIds.notnull}")
	private List<Integer> parentIds;

    public List<SysDeptTree> getDeptParents(){
        return Collections.copyToList(parentIds, v -> new SysDeptTree(getDeptId(), v));
    }
}
