package com.cowave.sys.admin.domain.rabc.request;

import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class ScopeStatusUpdate {

    /**
     * 权限id
     */
    private Integer scopeId;

    /**
     * 用户状态
     */
    private Integer scopeStatus;
}
