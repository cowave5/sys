/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author shanhuiming
 */
@NoArgsConstructor
@Data
public class OnlineUser {

    /**
     * Token id
     */
    private String tokenId;

    /**
     * 登录账号
     */
    private String userAccount;

    /**
     * 登录名称
     */
    private String userName;

    /**
     * 登录IP
     */
    private String accessIp;

    /**
     * 访问时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date accessTime;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

    /**
     * 访问集群
     */
    private String accessCluster;

    /**
     * 系统/用户
     */
    private String tokenType;

    /**
     * 开始时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public OnlineUser(AccessUserDetails userDetails){
        this.tokenId = userDetails.getId();
        this.tokenType = userDetails.getType();
        this.accessIp = userDetails.getAccessIp();
        this.accessTime = userDetails.getAccessTime();
        this.userAccount = userDetails.getUsername();
        this.userName = userDetails.getUserNick();
        this.loginTime = userDetails.getLoginTime();
        this.accessCluster = userDetails.getClusterName();
    }
}
