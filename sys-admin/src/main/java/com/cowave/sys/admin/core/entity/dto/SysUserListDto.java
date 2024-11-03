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

import com.cowave.sys.admin.core.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserListDto extends SysUser {

    /**
     * Ldap部门
     */
    private String ldapDept;

    /**
     * Ldap岗位
     */
    private String ldapPost;

    /**
     * 部门岗位
     */
    private List<SysUserDeptPost> deptPosts;

    @Data
    public static class SysUserDeptPost {

        /**
         * 部门id
         */
        private Long deptId;

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
        private Long postId;

        /**
         * 岗位名称
         */
        private String postName;
    }
}
