/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.entity.request;

import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class UserQuery {

    /**
     * 部门id
     */
    private Integer deptId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户电话
     */
    private String userPhone;

    /**
     * 用户职级
     */
    private String rank;

    /**
     * 分页索引
     */
    private Integer page = 1;

    /**
     * 分页大小
     */
    private Integer pageSize = 10;

    public Integer getOffset() {
        if (page == null || pageSize == null) {
            return null;
        }
        return this.page > 0 ? (this.page - 1) * this.pageSize : 0;
    }
}