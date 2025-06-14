/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.rabc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shanhuiming
 */
@NoArgsConstructor
@Data
public class SysDeptTree {

    /**
     * 部门id
     */
    @TableId(type = IdType.AUTO)
    private Integer deptId;

    /**
     * 上级部门id
     */
    private Integer parentId;

    /**
     * 关系类型
     */
    private Integer treeType;

    public SysDeptTree(Integer deptId, Integer parentId){
        this.deptId = deptId;
        this.parentId = parentId;
    }
}
