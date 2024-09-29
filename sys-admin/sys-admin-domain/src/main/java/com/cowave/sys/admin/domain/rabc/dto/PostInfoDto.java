package com.cowave.sys.admin.domain.rabc.dto;

import com.cowave.commons.framework.access.security.AccessInfoSetter;
import com.cowave.sys.admin.domain.rabc.SysPost;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PostInfoDto extends SysPost implements AccessInfoSetter {

    /**
     * 上级岗位id
     */
    private Integer parentId;
}
