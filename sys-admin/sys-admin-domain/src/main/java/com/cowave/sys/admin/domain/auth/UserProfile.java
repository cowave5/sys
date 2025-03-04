/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.auth;

import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.sys.admin.domain.rabc.constants.SysUserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Data
public class UserProfile {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户类型
     */
    private int userType;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户性别
     */
    private Integer userSex;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 旧密码
     */
    private String oldPasswd;

    /**
     * 新密码
     */
    private String newPasswd;

    /**
     * 用户电话
     */
    @Pattern(regexp = "^[1][3,4,5,7,8][0-9]{9}$", message = "{user.invalid.phone}")
    private String userPhone;

    /**
     * 用户邮箱
     */
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{user.invalid.email}")
    private String userEmail;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 是否OAuth用户
     */
    private int isoauth;

    /**
     * 部门/岗位
     */
    private List<String> depts = new ArrayList<>();

    /**
     * 上级用户
     */
    private List<String> parents = new ArrayList<>();

    /**
     * 用户角色
     */
    private List<String> roles = new ArrayList<>();

    /**
     * 用户权限
     */
    private List<String> permissions = new ArrayList<>();

    /**
     * ldap用户部门
     */
    private String ldapDept;

    /**
     * ldap用户岗位
     */
    private String ldapPost;

    /**
     * ldap用户上级
     */
    private String ldapLeader;

    public UserProfile(){

    }

    public UserProfile(AccessUserDetails userDetails){
        this.roles = userDetails.getRoles();
        this.permissions = userDetails.getPermissions();
        this.userName = userDetails.getUserNick();
    }

    public List<String> getDepts(){
        if(SysUserType.LDAP.isEqual(userType) && depts.isEmpty()){
            return List.of("Ldap: " + ldapDept + "/" + ldapPost);
        }
        return depts;
    }

    public List<String> getParents(){
        if(SysUserType.LDAP.isEqual(userType) && parents.isEmpty()){
            return List.of("Ldap: " + ldapLeader);
        }
        return parents;
    }

    public void setLdapInfo(LdapUser ldapUser){
        if(ldapUser == null){
            return;
        }
        this.ldapPost = ldapUser.getUserPost();
        this.ldapDept = ldapUser.getUserDept();
        this.ldapLeader = ldapUser.getUserLeader();
    }
}
