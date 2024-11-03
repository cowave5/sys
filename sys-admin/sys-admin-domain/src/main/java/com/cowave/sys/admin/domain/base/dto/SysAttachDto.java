/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 附件
 *
 * @author shanhuiming
 */
@Data
public class SysAttachDto {

    /**
     * 附件id
     */
    private Long attachId;

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

    /**
     * 附件名称
     */
    private String attachName;

    /**
     * 附件大小
     */
    private Long attachSize;

    /**
     * 附件路径
     */
    private String attachPath;

    /**
     * 预览地址
     */
    private String viewUrl;

    /**
     * 附件状态 0未生效 1已生效
     */
    private Integer attachStatus = 1;

    /**
     * 是否公开的 1是 0否
     */
    private Integer isPublic = 0;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;
}
