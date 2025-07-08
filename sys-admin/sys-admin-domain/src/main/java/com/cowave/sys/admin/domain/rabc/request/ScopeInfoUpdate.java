package com.cowave.sys.admin.domain.rabc.request;

import lombok.Data;

import java.util.Map;

/**
 * @author shanhuiming
 */
@Data
public class ScopeInfoUpdate {

    /**
     * 权限id
     */
    private Integer scopeId;

    /**
     * 权限内容
     */
    private Map<String, Object> scopeContent;
}
