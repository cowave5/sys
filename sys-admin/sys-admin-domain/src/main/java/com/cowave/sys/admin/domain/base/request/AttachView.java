package com.cowave.sys.admin.domain.base.request;

import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class AttachView {

    /**
     * 附件id
     */
    private Long attachId;

    /**
     * 预览地址
     */
    private String attachPath;
}
