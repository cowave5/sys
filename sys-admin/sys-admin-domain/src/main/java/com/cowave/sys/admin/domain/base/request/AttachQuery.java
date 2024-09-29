package com.cowave.sys.admin.domain.base.request;

import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class AttachQuery {

    private Long masterId;

    private String attachGroup;

    private String attachType;
}
