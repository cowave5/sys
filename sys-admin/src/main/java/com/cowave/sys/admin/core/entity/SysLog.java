/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author shanhuiming
 */
@TableName(autoResultMap = true)
@Data
public class SysLog {

    /**
     * 日志id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 部门id
     */
    private Integer deptId;

    /**
     * 日志模块
     */
    private String logGroup;

    /**
     * 日志类型
     */
    private String logType;

    /**
     * 日志动作
     */
    private String logAction;

    /**
     * 访问ip
     */
    private String ip;

    /**
     * 访问url
     */
    private String url;

    /**
     * 日志描述
     */
    private String logDesc;

    /**
     * 日志详情
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> logContent;

    /**
     * 日志状态
     */
    private Integer logStatus;

    /**
     * 日志时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date logTime;
}
