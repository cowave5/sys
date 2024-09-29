package com.cowave.sys.admin.domain.rabc.dto;

import com.cowave.commons.framework.access.security.AccessInfo;
import com.cowave.sys.admin.domain.rabc.SysPost;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PostInfoDto extends SysPost {

    /**
     * 上级岗位id
     */
    private Integer parentId;

    public void setCreateInfo(AccessInfo accessInfo){
        this.setCreateUser(accessInfo.getAccessUserId());
        this.setCreateDept(accessInfo.getAccessDeptId());
        this.setCreateTime(accessInfo.getAccessTime());
        this.setUpdateUser(accessInfo.getAccessUserId());
        this.setUpdateDept(accessInfo.getAccessDeptId());
        this.setUpdateTime(accessInfo.getAccessTime());
    }
}
