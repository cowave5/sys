/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.auth.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.client.http.response.HttpResponse;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationInfo;
import com.cowave.commons.framework.access.security.AccessInfo;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.sys.admin.domain.auth.OAuthClient;
import com.cowave.sys.admin.domain.auth.bo.GitlabUser;
import com.cowave.sys.admin.domain.auth.OAuthServer;
import com.cowave.sys.admin.domain.auth.bo.GitlabToken;
import com.cowave.sys.admin.domain.auth.OAuthUser;
import com.cowave.sys.admin.domain.auth.bo.OAuth2CodeBo;
import com.cowave.sys.admin.domain.auth.request.OAuth2CodeRequest;
import com.cowave.sys.admin.domain.auth.request.OAuth2TokenRequest;
import com.cowave.sys.admin.domain.auth.request.OAuthUserQuery;
import com.cowave.sys.admin.domain.auth.vo.OAuth2CodeVo;
import com.cowave.sys.admin.domain.base.SysOperation;
import com.cowave.sys.admin.domain.constants.SuccessStatus;
import com.cowave.sys.admin.domain.constants.UserType;
import com.cowave.sys.admin.domain.rabc.*;
import com.cowave.sys.admin.infra.auth.dao.*;
import com.cowave.sys.admin.infra.base.SysOperationHandler;
import com.cowave.sys.admin.infra.rabc.dao.*;
import com.cowave.sys.admin.service.auth.GitlabService;
import com.cowave.sys.admin.service.auth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.cowave.commons.client.http.constants.HttpCode.*;
import static com.cowave.sys.admin.domain.AdminRedisKeys.*;
import static com.cowave.sys.admin.domain.constants.OpAction.LOGIN;
import static com.cowave.sys.admin.domain.constants.OpAction.LOGIN_OAUTH;
import static com.cowave.sys.admin.domain.constants.OpModule.SYSTEM;
import static com.cowave.sys.admin.domain.constants.OpModule.SYSTEM_AUTH;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class OAuthServiceImpl implements OAuthService {
    private final RedisHelper redisHelper;
    private final SysTenantDao sysTenantDao;
    private final SysUserDao sysUserDao;
    private final SysRoleDao sysRoleDao;
    private final SysUserRoleDao sysUserRoleDao;
    private final SysUserTreeDao sysUserTreeDao;
    private final OAuthUserDao oauthUserDao;
    private final OAuthClientDao oAuthClientDao;
    private final OAuthServerDao oauthServerDao;
    private final SysOperationHandler sysOperationHandler;
    private final GitlabService gitlabService;
    private final UserDetailsDao userDetailsDao;

    @Override
    public AccessUserDetails gitlabCallback(String tenantId, String code) {
        // 根据授权码兑换令牌
        OAuthServer oAuthServer = oauthServerDao.getByServerType(tenantId, UserType.GITLAB.getVal());

        HttpResponse<GitlabToken> tokenResponse = gitlabService.getGitlabToken(oAuthServer.getAuthUrl(),
                oAuthServer.getAppId(), oAuthServer.getAppSecret(),
                oAuthServer.getRedirectUrl(), oAuthServer.getGrantType(),
                oAuthServer.getAuthScope(), code);
        HttpAsserts.isTrue(tokenResponse.isSuccess(), INTERNAL_SERVER_ERROR, tokenResponse.getMessage());

        GitlabToken gitlabToken = tokenResponse.getBody();
        assert gitlabToken != null;
        HttpResponse<GitlabUser> userResponse = gitlabService.getGitlabUser(oAuthServer.getAuthUrl(), gitlabToken.getAccessToken());
        HttpAsserts.isTrue(userResponse.isSuccess(), INTERNAL_SERVER_ERROR, userResponse.getMessage());
        GitlabUser gitlabUser = userResponse.getBody();

        // Gitlab用户信息
        assert gitlabUser != null;
        OAuthUser oauthUser = oauthUserDao.getByAccount(tenantId, UserType.GITLAB.getVal(), gitlabUser.getUsername());
        if (oauthUser != null) {
            oauthUser.setUserName(gitlabUser.getName());
            oauthUser.setUserEmail(gitlabUser.getEmail());
            oauthUser.setUserAvatar(gitlabUser.getAvatarUrl());
            oauthUser.setUpdateTime(new Date());
            if (CollectionUtils.isNotEmpty(gitlabUser.getIdentities())) {
                GitlabUser.LdapInfo ldap = gitlabUser.getIdentities().get(0);
                oauthUser.setUserDept(ldap.getExternUid());
            }
            oauthUserDao.updateById(oauthUser);
        } else {
            oauthUser = GitlabUser.oAuthUser(gitlabUser);
            oauthUser.setTenantId(tenantId);
            oauthUserDao.save(oauthUser);
        }

        // 对应系统用户
        String userCode = UserType.GITLAB.newCode(tenantId, oauthUser.getUserAccount());
        SysUser sysUser = sysUserDao.getByCode(userCode);
        if(sysUser == null){
            sysUser = new SysUser();
            sysUser.setUserCode(userCode);
            sysUser.setTenantId(tenantId);
            sysUser.setUserType(UserType.GITLAB);
            sysUser.setUserAccount(oauthUser.getUserAccount());
            sysUser.setUserName(oauthUser.getUserName());
            sysUser.setUserEmail(oauthUser.getUserEmail());
            sysUserDao.save(sysUser);
            // role
            SysRole sysRole = sysRoleDao.getByCode(tenantId, oAuthServer.getRoleCode());
            if(sysRole != null) {
                SysUserRole userRole = new SysUserRole(sysUser.getUserId(), sysRole.getRoleId());
                sysUserRoleDao.save(userRole);
            }
            // 用户关系
            SysUserTree userTree = new SysUserTree(sysUser.getUserId(), 0, tenantId);
            sysUserTreeDao.save(userTree);
            // 用户树缓存
            redisHelper.delete(USER_DIAGRAM + ":" + tenantId);
            redisHelper.delete(DEPT_USER_DIAGRAM + ":" + tenantId);
        }else{
            sysUser.setUserName(oauthUser.getUserName());
            sysUser.setUserEmail(oauthUser.getUserEmail());
            sysUserDao.updateGitlabByCode(sysUser);
        }

        // 创建令牌
        SysTenant sysTenant = sysTenantDao.getById(tenantId);
        AccessUserDetails userDetails = userDetailsDao.newUserDetails(UserType.GITLAB.getVal(), sysTenant, sysUser);
        // 登录日志
        OperationInfo operationInfo = OperationInfo.builder()
                .success(true)
                .opModule(SYSTEM)
                .opType(SYSTEM_AUTH)
                .opAction(LOGIN)
                .desc("Gitlab登录：" + oauthUser.getUserAccount())
                .build();
        sysOperationHandler.create(operationInfo, null);
        return userDetails;
    }

    @Override
    public OAuthServer getServerConfig(String tenantId, String serverType) {
        return oauthServerDao.getByServerType(tenantId, serverType);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateServerConfig(String tenantId, OAuthServer oauthServer) {
        oauthServerDao.removeByServerType(tenantId, oauthServer.getServerType());
        oauthServer.setTenantId(tenantId);
        oauthServerDao.save(oauthServer);
    }

    @Override
    public Page<OAuthUser> listUser(String tenantId, OAuthUserQuery query) {
        return oauthUserDao.getUserPage(tenantId, query.getServerType(), query.getUserAccount());
    }

    @Override
    public Page<OAuthClient> listClient(String tenantId, String clientName) {
        return oAuthClientDao.page(tenantId, clientName);
    }

    @Override
    public OAuthClient createClient(String tenantId, OAuthClient oAuthClient) {
        oAuthClient.setTenantId(tenantId);
        oAuthClient.setClientId(UUID.randomUUID().toString().replace("-", ""));
        oAuthClient.setClientSecret(UUID.randomUUID().toString().replace("-", ""));
        oAuthClientDao.save(oAuthClient);
        return oAuthClient;
    }

    @Override
    public void deleteClient(String tenantId, List<Integer> ids) {
        oAuthClientDao.removeByIds(tenantId, ids);
    }

    @Override
    public OAuth2CodeVo getClientCode(OAuth2CodeRequest request) {
        // 验证客户端id
        OAuthClient oAuthClient = oAuthClientDao.getByClientId(request.getClientId());
        HttpAsserts.notNull(oAuthClient, BAD_REQUEST, "{admin.oauth.name.not.exist}");

        // 用户是否允许授权这个客户端
        HttpAsserts.equals(Access.tenantId(), oAuthClient.getTenantId(),
                FORBIDDEN, "{admin.oauth.resp.forbidden}");

        // 校验返回类型
        HttpAsserts.isTrue(StringUtils.equalsIgnoreCase("code", request.getResponseType()),
                BAD_REQUEST, "{admin.oauth.resp.invalid}");

        // 验证授权类型
        HttpAsserts.isTrue(oAuthClient.getGrantType().contains("authorization_code"),
                BAD_REQUEST, "{admin.oauth.grant.invalid}");

        // 验证回调地址
        HttpAsserts.isTrue(StringUtils.equalsIgnoreCase(oAuthClient.getRedirectUrl(), request.getRedirectUri()),
                BAD_REQUEST, "{admin.oauth.redirect.invalid}");

        // 生成随机code
        String code = UUID.randomUUID().toString().replace("-", "");

        OAuth2CodeBo oAuth2CodeBo = new OAuth2CodeBo();
        oAuth2CodeBo.setUserCode(Access.userCode());
        oAuth2CodeBo.setState(request.getState());
        oAuth2CodeBo.setRedirectUri(oAuthClient.getRedirectUrl());

        // PKCE校验（这里简单示意下只支持md5）
        if(StringUtils.isNotBlank(request.getCodeChallenge())
                && "md5".equalsIgnoreCase(request.getCodeChallengeMethod())){
            String codeVerifier = SecureUtil.md5(request.getCodeChallenge());
            oAuth2CodeBo.setCodeVerifier(codeVerifier);
        }

        // 绑定用户信息
        redisHelper.putExpire(AUTH_OAUTH.formatted(code), oAuth2CodeBo, 60, TimeUnit.SECONDS);
        return new OAuth2CodeVo(code, oAuthClient.getClientName(), oAuthClient.getAuthScope());
    }

    @Override
    public void clientRedirect(String code, HttpServletResponse response) throws IOException {
        OAuth2CodeBo oAuth2CodeBo = redisHelper.getValue(AUTH_OAUTH.formatted(code));
        HttpAsserts.notNull(oAuth2CodeBo, BAD_REQUEST, "{admin.oauth.code.expire}");
        // 回调
        String redirectUrl = oAuth2CodeBo.getRedirectUri() + "?code=" + code + "&state=" + oAuth2CodeBo.getState();
        response.sendRedirect(redirectUrl);
    }

    @Override
    public AccessUserDetails getClientToken(OAuth2TokenRequest request) {
        // 获取用户code
        OAuth2CodeBo oAuth2CodeBo = redisHelper.getValue(AUTH_OAUTH.formatted(request.getCode()));
        HttpAsserts.notNull(oAuth2CodeBo, BAD_REQUEST, "{admin.oauth.code.expire}");

        // 验证客户端id
        OAuthClient oAuthClient = oAuthClientDao.getByClientId(request.getClientId());
        HttpAsserts.notNull(oAuthClient, BAD_REQUEST, "{admin.oauth.name.not.exist}");

        // 验证回调地址
        HttpAsserts.isTrue(StringUtils.equalsIgnoreCase(oAuthClient.getRedirectUrl(), request.getRedirectUri()),
                BAD_REQUEST, "{admin.oauth.redirect.invalid}");

        // 验证客户端密钥
        HttpAsserts.isTrue(StringUtils.equals(request.getClientSecret(), oAuthClient.getClientSecret()),
                BAD_REQUEST, "{admin.oauth.secret.invalid}");

        // PKCE校验
        if(StringUtils.isNotBlank(oAuth2CodeBo.getCodeVerifier())){
            HttpAsserts.isTrue(StringUtils.equals(oAuth2CodeBo.getCodeVerifier(), request.getCodeVerifier()),
                BAD_REQUEST, "{admin.oauth.pkce.invalid}");
        }

        // 创建令牌
        SysUser sysUser = sysUserDao.getByCode(oAuth2CodeBo.getUserCode());
        SysTenant sysTenant = sysTenantDao.getById(sysUser.getTenantId());
        String authType = sysUser.getUserType().oauthType();
        AccessUserDetails userDetails = userDetailsDao.newUserDetails(authType, sysTenant, sysUser);

        // 授权日志
        SysOperation sysOperation = new SysOperation();
        sysOperation.setOpStatus(SuccessStatus.SUCCESS);
        sysOperation.setOpModule(SYSTEM);
        sysOperation.setOpType(SYSTEM_AUTH);
        sysOperation.setOpAction(LOGIN_OAUTH);
        sysOperation.setOpDesc("授权应用'" + oAuthClient.getClientName() + "'访问");
        sysOperation.setAccess(new AccessInfo(userDetails));
        sysOperation.setIp(Access.accessIp());
        sysOperation.setUrl(Access.accessMethod() + " " + Access.accessUrl());
        sysOperation.setOpTime(Access.accessTime());
        sysOperationHandler.create(sysOperation);
        return userDetails;
    }
}
