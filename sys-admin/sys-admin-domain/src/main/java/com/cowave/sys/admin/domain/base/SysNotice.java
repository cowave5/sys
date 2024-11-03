/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Data
public class SysNotice {

    /**
     * 公告id
     */
    @TableId(type = IdType.AUTO)
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
     * 公告内容
     */
    private String content;

    /**
     * 是否系统公告
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
     * 是否全员
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
    private List<Integer> goalsUser;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建部门
     */
    private Integer createDept;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private Integer updateUser;

    /**
     * 更新部门
     */
    private Integer updateDept;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
