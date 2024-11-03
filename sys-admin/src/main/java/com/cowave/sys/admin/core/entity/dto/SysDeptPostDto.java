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

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 部门岗位
 *
 * @author shanhuiming
 *
 */
@Data
public class SysDeptPostDto {

    /**
     * 部门id
     */
    @NotNull(message = "{dept.notnull.id}")
    private Integer deptId;

    /**
     * 部门编码
     */
    private String deptCode;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 岗位id
     */
    @NotNull(message = "{post.notnull.id}")
    private Integer postId;

    /**
     * 岗位编码
     */
    private String postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 是否部门默认岗位
     */
    private Integer isDefault = 0;
}
