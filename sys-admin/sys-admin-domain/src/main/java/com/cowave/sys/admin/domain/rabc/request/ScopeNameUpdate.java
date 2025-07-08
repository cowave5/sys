package com.cowave.sys.admin.domain.rabc.request;

import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class ScopeNameUpdate {

    /**
     * 权限id
     */
    private Integer scopeId;

    /**
     * 权限名称
     */
    private String scopeName;
}
