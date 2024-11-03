/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.oauth.service.impl;

import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.BearerTokenService;
import com.cowave.commons.framework.access.security.Permission;
import com.cowave.sys.admin.oauth.domain.GitlabUser;
import com.cowave.sys.admin.oauth.domain.OAuthConfig;
import com.cowave.sys.admin.oauth.domain.OAuthToken;
import com.cowave.sys.admin.oauth.domain.OAuthUser;
import com.cowave.sys.admin.oauth.mapper.OAuthMapper;
import com.cowave.sys.admin.oauth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class OAuthServiceImpl implements OAuthService {

    private final RestTemplate restTemplate;

    private final BearerTokenService bearerTokenService;

    private final OAuthMapper oauthMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveConfig(OAuthConfig oAuthConfig) {
        oauthMapper.deleteConfig(oAuthConfig.getAppType());
        oauthMapper.saveConfig(oAuthConfig);
    }

    @Override
    public OAuthConfig getConfig(String appType) {
        return oauthMapper.getConfig(appType);
    }

    @Override
    public List<OAuthConfig> listConfig() {
        return oauthMapper.listConfig();
    }

    @Override
    public List<OAuthUser> listUser(OAuthUser oAuthUser) {
        return oauthMapper.listUser(oAuthUser);
    }

    @Override
    public OAuthUser infoUser(Integer id) {
        return oauthMapper.infoUser(id);
    }

    @Override
    public void changeUserRole(Integer userId, Integer roleId) {
        oauthMapper.changeUserRole(userId, roleId, new Date());
    }

    @Override
    public void deleteUser(Integer userId) {
        oauthMapper.deleteUser(userId);
    }

    @Override
    public AccessUserDetails gitlabCallback(String code) {
        OAuthConfig oAuthConfig = oauthMapper.getConfig(OAuthConfig.APP_GITLAB);
        OAuthToken oauthToken = restTemplate.postForObject(oAuthConfig.gitlabTokenUrl(code), null, OAuthToken.class);
        assert oauthToken != null;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + oauthToken.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<GitlabUser> response =
                restTemplate.exchange(oAuthConfig.gitlabUserUrl(), HttpMethod.GET, entity, GitlabUser.class);
        GitlabUser gitlabUser = response.getBody();
        assert gitlabUser != null;

        OAuthUser oAuthUser = GitlabUser.oAuthUser(gitlabUser);
        oAuthUser.setUserRole(oAuthConfig.getUserRole());
        oauthMapper.saveUser(oAuthUser); // 不更新role

        // 获取用户当前角色及对应权限
        String roleCode = oauthMapper.roleCode(oAuthUser.getId());
        List<String> permits;
        if(Permission.ROLE_ADMIN.equals(roleCode)){
            permits = List.of(Permission.PERMIT_ADMIN);
        }else{
            permits = oauthMapper.permits(oAuthUser.getId());
        }

        AccessUserDetails userDetails = OAuthUser.newUserDetails(oAuthUser);
        userDetails.setPermissions(permits);
        userDetails.setRoles(List.of(roleCode));
        bearerTokenService.dualAssignToken(userDetails);
        return userDetails;
    }
}
