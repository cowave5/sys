/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.auth.impl;

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.BearerTokenService;
import com.cowave.commons.framework.access.security.Permission;
import com.cowave.sys.admin.domain.auth.bo.GitlabUser;
import com.cowave.sys.admin.domain.auth.OAuthConfig;
import com.cowave.sys.admin.domain.auth.bo.GitlabToken;
import com.cowave.sys.admin.domain.auth.OAuthUser;
import com.cowave.sys.admin.domain.auth.dto.OAuthUserDto;
import com.cowave.sys.admin.domain.auth.request.OAuthUserQuery;
import com.cowave.sys.admin.infra.auth.dao.OAuthConfigDao;
import com.cowave.sys.admin.infra.auth.dao.OAuthUserDao;
import com.cowave.sys.admin.infra.auth.dao.mapper.dto.OAuthUserDtoMapper;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysRoleDtoMapper;
import com.cowave.sys.admin.service.auth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.cowave.sys.admin.domain.auth.enums.OAuthApp.GITLAB;
import static com.cowave.sys.admin.domain.rabc.enums.UserType.OAUTH_GITLAB;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class OAuthServiceImpl implements OAuthService {
    private final BearerTokenService bearerTokenService;
    private final RestTemplate restTemplate;
    private final OAuthUserDao oauthUserDao;
    private final OAuthConfigDao oauthConfigDao;
    private final OAuthUserDtoMapper oauthUserDtoMapper;
    private final SysRoleDtoMapper sysRoleDtoMapper;

    @Override
    public AccessUserDetails gitlabCallback(String code) {
        OAuthConfig config = oauthConfigDao.queryByAppType(GITLAB.val());
        GitlabToken gitlabToken = restTemplate.postForObject(GITLAB.getTokenUrl(config, code), null, GitlabToken.class);

        assert gitlabToken != null;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + gitlabToken.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<GitlabUser> response =
                restTemplate.exchange(GITLAB.getUserUrl(config), HttpMethod.GET, entity, GitlabUser.class);
        GitlabUser gitlabUser = response.getBody();

        assert gitlabUser != null;
        OAuthUser oauthUser = GitlabUser.oAuthUser(gitlabUser);
        oauthUser.setUserCode(OAUTH_GITLAB.generateCode());
        oauthUser.setRoleCode(config.getRoleCode());
        oauthUserDtoMapper.insertUpdateUser(oauthUser);

        // 获取用户当前角色及对应权限
        String roleCode = oauthUserDao.getRoleCodeByAccount(OAUTH_GITLAB.val(), oauthUser.getUserAccount());
        List<String> permits;
        if(Permission.ROLE_ADMIN.equals(roleCode)){
            permits = List.of(Permission.PERMIT_ADMIN);
        }else{
            permits = sysRoleDtoMapper.getPermitsByRoleCode(roleCode);
        }

        AccessUserDetails userDetails = OAuthUser.newUserDetails(oauthUser);
        userDetails.setPermissions(permits);
        userDetails.setRoles(List.of(roleCode));
        bearerTokenService.dualAssignToken(userDetails);
        return userDetails;
    }

    @Override
    public List<OAuthConfig> queryEnabledList() {
        return oauthConfigDao.queryEnabledList();
    }

    @Override
    public OAuthConfig queryByAppType(String appType) {
        return oauthConfigDao.queryByAppType(appType);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateConfig(OAuthConfig oauthConfig) {
        oauthConfigDao.removeByAppType(oauthConfig.getAppType());
        oauthConfigDao.save(oauthConfig);
    }

    @Override
    public List<OAuthUserDto> listUser(OAuthUserQuery query) {
        return oauthUserDtoMapper.listUser(Access.page(), query);
    }

    @Override
    public OAuthUser infoUser(Integer id) {
        return oauthUserDao.getById(id);
    }

    @Override
    public void deleteUser(Integer userId) {
        oauthUserDao.removeById(userId);
    }

    @Override
    public void changeUserRole(Integer userId, String roleCode) {
        oauthUserDao.updateRoleCodeById(userId, roleCode);
    }
}
