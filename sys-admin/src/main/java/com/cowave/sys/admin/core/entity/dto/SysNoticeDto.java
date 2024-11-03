/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.entity.dto;

import com.cowave.commons.framework.access.security.AccessInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 公告
 *
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class SysNoticeDto extends AccessInfo {

    /** 草稿 **/
    public static final int STATUS_DRAFT = 0;

    /** 已发布 **/
    public static final int STATUS_PUBLISH = 1;

	/** 已撤回 **/
	public static final int STATUS_RECALL = 2;

	/**
	 * 公告id
	 */
	private Long noticeId;

	/**
	 * 公告标题
	 */
	@NotNull(message = "{notice.notnull.title}")
	private String noticeTitle;

	/**
	 * 公告状态 0草稿 1已发布 2已撤回
	 */
    private Integer noticeStatus;

	/**
	 * 公告类型 0 公告 1 通知
	 */
	private Integer noticeType;

	/**
	 * 公告等级 0 普通 1 紧急
	 */
	private Integer noticeLevel;

	/**
	 * 公告内容
	 */
	private String content;

	/**
	 * 是否系统公告 0 否 1 是
	 */
	private Integer isSystem;

	/**
	 * 总人数
	 */
	private Integer statTotal;

	/**
     * 已读人数
     */
    private Integer statRead;

    /**
     * 目标是否全员  0 否 1 是
     */
    private Integer goalsAll;

    /**
     * 目标单位
     */
    private List<Long> goalsDept;

    /**
     * 目标角色
     */
    private List<Long> goalsRole;

    /**
     * 目标用户
     */
    private List<Long> goalsUser;

	/**
	 * 发布时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date publishTime;

	/**
	 * 图片附件
	 */
	private List<SysAttachDto> attachs;

	/**
	 * 创建人
	 */
	private Long createUser;

	/**
	 * 创建部门
	 */
	private Long createDept;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 更新人
	 */
	private Long updateUser;

	/**
	 * 更新部门
	 */
	private Long updateDept;

	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	/**
	 * 创建人名称
	 */
	private String createUserName;

    /**
     * 已读状态
     */
    private Integer readStatus;

    /**
     * 已读反馈
     */
    private String readBack;

    /**
     * 已读时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date readTime;
}
