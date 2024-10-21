/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.api.service.impl;

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.security.AccessToken;
import com.cowave.commons.tools.Asserts;
import com.cowave.sys.admin.api.service.ProfileService;
import com.cowave.sys.admin.core.entity.UserProfile;
import com.cowave.sys.admin.core.entity.ldap.LdapUser;
import com.cowave.sys.admin.core.mapper.LdapMapper;
import com.cowave.sys.admin.core.mapper.ProfileMapper;
import com.cowave.sys.model.admin.SysUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileMapper profileMapper;

    private final LdapMapper ldapMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserProfile info() {
        if(AccessToken.TYPE_OAUTH.equals(Access.tokenType())){
            return profileMapper.oauthInfo(Access.userId());
        }else{
            UserProfile userProfile = profileMapper.info(Access.userId());
            if(SysUser.TYPE_LDAP == userProfile.getUserType()){
                LdapUser ldapUser = ldapMapper.ldapUserInfo(userProfile.getUserId());
                userProfile.setLdapInfo(ldapUser);
            }
            return userProfile;
        }
    }

    @Override
    public void edit(UserProfile userProfile) {
        userProfile.setUserId(Access.userId());
        userProfile.setUserAccount(Access.userAccount());
        userProfile.setCreateTime(new Date());
        profileMapper.edit(userProfile);
    }

    @Override
    public void resetPasswd(UserProfile userProfile) {
        String passwd = profileMapper.queryPasswd(Access.userId());
        Asserts.isTrue(passwordEncoder.matches(userProfile.getOldPasswd(), passwd), "{user.pwd.failed}");
        Asserts.isFalse(passwordEncoder.matches(userProfile.getNewPasswd(), passwd), "{user.pwd.repeat}");
        profileMapper.resetPasswd(Access.userId(), passwordEncoder.encode(userProfile.getNewPasswd()));
    }
}
