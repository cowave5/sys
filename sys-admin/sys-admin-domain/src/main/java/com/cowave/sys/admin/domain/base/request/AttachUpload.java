package com.cowave.sys.admin.domain.base.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shanhuiming
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttachUpload {

    /**
     * 是否公开
     */
    private int isPublic;

    /**
     * 宿主id
     */
    private Long masterId;

    /**
     * 附件分组
     */
    private String attachGroup;

    /**
     * 附件类型
     */
    private String attachType;
}
