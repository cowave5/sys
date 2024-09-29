package com.cowave.sys.admin.domain.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Data
public class NoticeMsgDto {

    /**
     * 已读状态
     */
    private Integer readStatus;

    /**
     * 已读反馈
     */
    private String readBack;

    /**
     * 公告id
     */
    private Long noticeId;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 公告状态
     */
    private Integer noticeStatus;

    /**
     * 公告类型
     */
    private Integer noticeType;

    /**
     * 公告等级
     */
    private Integer noticeLevel;

    /**
     * 是否系统公告
     */
    private Integer isSystem;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    /**
     * 创建人
     */
    private String createBy;
}
