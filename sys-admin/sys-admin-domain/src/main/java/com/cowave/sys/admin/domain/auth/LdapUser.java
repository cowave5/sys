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
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ldap用户信息
 */
@Data
public class LdapUser {

    private static final Pattern PATTERN_CN = Pattern.compile("CN=([^,]+)");

    /**
     * 用户id
     */
    private Long id;

    /**
     * 内部用户id
     */
    private Integer userId;

    /**
     * Ldap配置id
     */
    private Integer ldapId;

    /**
     * 用户信息
     */
    private String userInfo;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPasswd;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户电话
     */
    private String userPhone;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户岗位
     */
    private String userPost;

    /**
     * 用户部门
     */
    private String userDept;

    /**
     * 上级用户
     */
    private String userLeader;

    /**
     * 是否与内部用户信息同步 1 是 0 否
     */
    private Integer isSync;

    /**
     * 创建时间
     */
    private Date createTime = new Date();

    /**
     * 更新时间
     */
    private Date updateTime;

    public void valueUserAccount(Attribute attribute, LdapConfig ldapConfig) throws NamingException {
        if(ldapConfig.getAccountProperty().equals(attribute.getID())){
            this.userAccount = attribute.get().toString();
        }
    }

    public void valueUserName(Attribute attribute, LdapConfig ldapConfig) throws NamingException {
        if(StringUtils.isBlank(ldapConfig.getNameProperty())){
            return;
        }
        if(ldapConfig.getNameProperty().equals(attribute.getID())){
            this.userName = attribute.get().toString();
        }
    }

    public void valueUserPhone(Attribute attribute, LdapConfig ldapConfig) throws NamingException {
        if(StringUtils.isBlank(ldapConfig.getPhoneProperty())){
            return;
        }
        if(ldapConfig.getPhoneProperty().equals(attribute.getID())){
            this.userPhone = attribute.get().toString();
        }
    }

    public void valueUserEmail(Attribute attribute, LdapConfig ldapConfig) throws NamingException {
        if(StringUtils.isBlank(ldapConfig.getEmailProperty())){
            return;
        }
        if(ldapConfig.getEmailProperty().equals(attribute.getID())){
            this.userEmail = attribute.get().toString();
        }
    }

    public void valueUserPost(Attribute attribute, LdapConfig ldapConfig) throws NamingException {
        if(StringUtils.isBlank(ldapConfig.getPostProperty())){
            return;
        }
        if(ldapConfig.getPostProperty().equals(attribute.getID())){
            this.userPost = attribute.get().toString();
        }
    }

    public void valueDepartment(Attribute attribute, LdapConfig ldapConfig) throws NamingException {
        if(StringUtils.isBlank(ldapConfig.getDeptProperty())){
            return;
        }
        if(ldapConfig.getDeptProperty().equals(attribute.getID())){
            this.userDept = attribute.get().toString();
        }
    }

    public void valueUserLeader(Attribute attribute, LdapConfig ldapConfig) throws NamingException {
        if(StringUtils.isBlank(ldapConfig.getLeaderProperty())){
            return;
        }
        if(ldapConfig.getLeaderProperty().equals(attribute.getID())){
            String manager = attribute.get().toString();
            Matcher matcher = PATTERN_CN.matcher(manager);
            if (matcher.find()) {
                userLeader = matcher.group(1);
            }
        }
    }

    public void valueUserInfo(Attribute attribute, LdapConfig ldapConfig) throws NamingException {
        if(StringUtils.isBlank(ldapConfig.getInfoProperty())){
            return;
        }
        if(ldapConfig.getInfoProperty().equals(attribute.getID())){
            this.userInfo = attribute.get().toString();
        }
    }

    public static AccessUserDetails newUserDetails(LdapUser ldapUser){
        AccessUserDetails userDetails = AccessUserDetails.newUserDetails();
        userDetails.setType(AccessUserDetails.TYPE_LDAP);
        userDetails.setUserId(ldapUser.userId);
        userDetails.setUsername(ldapUser.userAccount);
        userDetails.setUserNick(ldapUser.userName);
        userDetails.setDeptName(ldapUser.userDept);
        return userDetails;
    }
}
