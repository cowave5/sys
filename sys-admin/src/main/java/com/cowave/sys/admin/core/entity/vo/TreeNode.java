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

import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class TreeNode {

    /**
     * id
     */
    private String id;

    /**
     * 上级id
     */
    private String pid;

    /**
     * 名称
     */
    private String label;

    /**
     * 内容
     */
    private String content;
}