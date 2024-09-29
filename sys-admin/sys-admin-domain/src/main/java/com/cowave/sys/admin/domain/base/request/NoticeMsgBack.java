package com.cowave.sys.admin.domain.base.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shanhuiming
 */
@Data
public class NoticeMsgBack {

    /**
     * 公告id
     */
    @NotNull(message = "{notice.notnull.id}")
    private Long noticeId;

    private String readBack;
}
