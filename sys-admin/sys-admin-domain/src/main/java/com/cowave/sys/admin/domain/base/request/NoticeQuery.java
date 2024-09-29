package com.cowave.sys.admin.domain.base.request;

import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class NoticeQuery {

    /**
	 * 公告标题
	 */
	private String noticeTitle;

    /**
	 * 公告类型 0 公告 1 通知
	 */
	private Integer noticeType;

    /**
	 * 公告状态 0草稿 1已发布 2已撤回
	 */
    private Integer noticeStatus;
}
