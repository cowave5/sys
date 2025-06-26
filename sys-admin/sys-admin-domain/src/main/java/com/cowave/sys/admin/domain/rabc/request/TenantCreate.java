package com.cowave.sys.admin.domain.rabc.request;

import com.cowave.sys.admin.domain.rabc.SysTenant;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shanhuiming
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TenantCreate extends SysTenant {

    /**
     * logo id
     */
    private Long attachId;
}
