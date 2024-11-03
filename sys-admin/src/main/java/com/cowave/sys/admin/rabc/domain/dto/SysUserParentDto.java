/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.rabc.domain.dto;

import lombok.Data;

/**
 * 用户关系
 *
 * @author shanhuiming
 *
 */
@Data
public class SysUserParentDto {

    /**
     * 关系类型
     */
    private Integer treeType;

    /**
     * 上级用户id
     */
    private Long parentId;

    /**
     * 上级用户编码
     */
    private String parentCode;

    /**
     * 上级用户名称
     */
    private String parentName;
}
