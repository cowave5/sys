/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.api.service;

import com.cowave.commons.framework.access.security.AccessToken;
import com.cowave.sys.admin.core.entity.ldap.LdapConfig;
import com.cowave.sys.admin.core.entity.ldap.LdapUser;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
public interface LdapService {

    /**
     * Ldap认证
     */
    AccessToken authenticate(String userAccount, String passWord);

    /**
     * 列表
     */
    List<LdapConfig> list(LdapConfig ldapConfig);

    /**
     * 详情
     */
    LdapConfig info(Integer id);

    /**
     * 新增
     */
    void add(LdapConfig ldapConfig);

    /**
     * 修改
     */
    void edit(LdapConfig ldapConfig);

    /**
     * 删除
     */
    void delete(Integer[] id);

    /**
     * 修改状态
     */
    void changeStatus(Integer id, Integer status);

    /**
     * 测试Ldap
     */
    void valid(LdapConfig ldapConfig);

    /**
     * Ldap用户信息
     */
    LdapUser userInfo(Long userId);
}
