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

import com.cowave.commons.framework.access.security.AccessToken;
import com.cowave.commons.framework.access.security.Permission;
import com.cowave.commons.framework.access.security.TokenService;
import com.cowave.commons.tools.Asserts;
import com.cowave.commons.tools.AssertsException;
import com.cowave.sys.admin.api.service.LdapService;
import com.cowave.sys.admin.core.entity.ldap.LdapAttributesMapper;
import com.cowave.sys.admin.core.entity.ldap.LdapConfig;
import com.cowave.sys.admin.core.entity.ldap.LdapUser;
import com.cowave.sys.admin.core.mapper.LdapMapper;
import com.cowave.sys.admin.core.mapper.SysRoleMapper;
import com.cowave.sys.admin.core.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DirContextAuthenticationStrategy;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.directory.SearchControls;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class LdapServiceImpl implements LdapService {

    private final TokenService tokenService;

    private final SysUserMapper sysUserMapper;

    private final SysRoleMapper sysRoleMapper;

    private final LdapMapper ldapMapper;

    private final BCryptPasswordEncoder bcryptPasswordEncoder;

    private final ObjectProvider<DirContextAuthenticationStrategy> dirContextAuthenticationStrategy;

    private LdapTemplate getLdapTemplate(LdapConfig ldapConfig){
        LdapContextSource source = new LdapContextSource();
        dirContextAuthenticationStrategy.ifUnique(source::setAuthenticationStrategy);
        PropertyMapper propertyMapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
        try{
            propertyMapper.from(ldapConfig.getLdapUser()).to(source::setUserDn);
            propertyMapper.from(ldapConfig.getLdapPasswd()).to(source::setPassword);
            propertyMapper.from(ldapConfig.anonymousReadOnly()).to(source::setAnonymousReadOnly);
            propertyMapper.from(ldapConfig.getBaseDn()).to(source::setBase);
            propertyMapper.from(ldapConfig.determineUrls()).to(source::setUrls);
            propertyMapper.from(ldapConfig.getEnvironment()).to(
                    baseEnvironment -> source.setBaseEnvironmentProperties(Collections.unmodifiableMap(baseEnvironment)));
            source.afterPropertiesSet();
        }catch(Exception e){
            throw new AssertsException("{ldap.invalid}", e);
        }
        return new LdapTemplate(source);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AccessToken authenticate(String userAccount, String passWord) {
        LdapConfig ldapConfig = ldapMapper.getEnableLdap();
        if(ldapConfig == null){
            return null;
        }

        LdapTemplate ldapTemplate = getLdapTemplate(ldapConfig);
        String filter = "(&(objectClass=" + ldapConfig.getUserClass() + ")(" + ldapConfig.getAccountProperty() + "=" + userAccount + "))";
        boolean isAuthenticated = ldapTemplate.authenticate("", filter, passWord);
        Asserts.isTrue(isAuthenticated, "{auth.passwd}");

        // 首次Ldap认证成功，创建SysUser
        List<LdapUser> list = ldapTemplate.search(
                ldapConfig.getUserDn(), filter, SearchControls.SUBTREE_SCOPE, new LdapAttributesMapper(ldapConfig));
        LdapUser ldapUser = list.get(0);
        ldapUser.setUserPasswd(bcryptPasswordEncoder.encode(passWord));
        ldapMapper.createSysUser(ldapUser);
        ldapMapper.grantSysRole(ldapUser.getUserId(), ldapConfig.getUserRole());

        // 留存LdapUser原始数据
        ldapUser.setLdapId(ldapConfig.getId());
        ldapUser.setUserPasswd(Base64.getEncoder().encodeToString(passWord.getBytes(StandardCharsets.UTF_8)));
        ldapMapper.saveLdapUser(ldapUser);

        String roleCode = sysRoleMapper.queryCodeById(ldapConfig.getUserRole());
        List<String> permits;
        if(Permission.ROLE_ADMIN.equals(roleCode)){
            permits = List.of(Permission.PERMIT_ADMIN);
        }else{
            permits = sysUserMapper.permitKeys(ldapUser.getUserId());
        }

        // 构造AccessToken
        AccessToken accessToken = LdapUser.accessToken(ldapUser);
        accessToken.setRoles(List.of(roleCode));
        accessToken.setPermissions(permits);
        tokenService.assignToken(accessToken);
        return accessToken;
    }

    @Override
    public List<LdapConfig> list(LdapConfig ldapConfig) {
        return ldapMapper.list(ldapConfig);
    }

    @Override
    public LdapConfig info(Integer id) {
        return ldapMapper.info(id);
    }

    @Override
    public void add(LdapConfig ldapConfig) {
        ldapMapper.insert(ldapConfig);
    }

    @Override
    public void edit(LdapConfig ldapConfig) {
        ldapMapper.update(ldapConfig);
    }

    @Override
    public void delete(Integer[] id) {
        ldapMapper.delete(id);
    }

    @Override
    public void changeStatus(Integer id, Integer status) {
        if(status == 1){
            Asserts.equals(0, ldapMapper.enableCount(id), "{ldap.enable.one}");
        }
        ldapMapper.changeStatus(id, status);
    }

    @Override
    public void valid(LdapConfig ldapConfig) {
        LdapTemplate ldapTemplate = getLdapTemplate(ldapConfig);
        String filter = "(&(objectClass=" + ldapConfig.getUserClass() + ")(" + ldapConfig.getAccountProperty() + "=*))";
        List<LdapUser> list;
        try{
            list = ldapTemplate.search(ldapConfig.getUserDn(),
                    filter, SearchControls.SUBTREE_SCOPE, new LdapAttributesMapper(ldapConfig));
        }catch(Exception e){
            throw new AssertsException("{ldap.failed.exception}", e);
        }
        if(list.isEmpty()){
            throw new AssertsException("{ldap.failed.user}");
        }
    }

    @Override
    public LdapUser userInfo(Long userId) {
        return ldapMapper.ldapUserInfo(userId);
    }
}
