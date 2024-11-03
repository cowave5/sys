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

/**
 * 公告已读
 *
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class SysNoticeReadDto extends AccessInfo {

    /** 消息未读 **/
    public static final int STATUS_PUBLISH_UNREAD = 0;

    /** 消息未读删除 **/
    public static final int STATUS_DELETE_UNREAD = 1;

    /** 消息未读撤回 **/
    public static final int STATUS_RECALL_UNREAD = 2;

    /** 消息已读 **/
    public static final int STATUS_PUBLISH_READ = 10;

    /** 消息已读删除 **/
    public static final int STATUS_DELETE_READ = 11;

    /** 消息已读撤回 **/
    public static final int STATUS_RECALL_READ = 22;

    /**
     * id
     */
    private Long id;

    /**
     * 公告id
     */
    @NotNull(message = "{notice.notnull.id}")
    private Long noticeId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 已读状态 0 否 1 是 2 read删除 3 notice删除
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

    /**
     * 用户名称
     */
    private String userName;
}
