/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.rabc.dto;

import com.cowave.sys.admin.domain.rabc.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserListDto extends SysUser {

    /**
     * 部门岗位
     */
    private List<SysUserDeptPost> deptPosts;

    @Data
    public static class SysUserDeptPost {

        /**
         * 部门id
         */
        private Integer deptId;

        /**
         * 部门名称
         */
        private String deptName;

        /**
         * 是否用户默认单位
         */
        private Integer isDefault = 0;

        /**
         * 是否单位负责人
         */
        private Integer isLeader = 0;

        /**
         * 岗位id
         */
        private Integer postId;

        /**
         * 岗位名称
         */
        private String postName;
    }
}
