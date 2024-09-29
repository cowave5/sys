/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.app.auth;

import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.Operation;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.configuration.ApplicationProperties;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.sys.admin.domain.auth.request.LdapRequest;
import com.cowave.sys.admin.domain.auth.vo.AuthInfo;
import com.cowave.sys.admin.domain.auth.vo.CaptchaInfo;
import com.cowave.sys.admin.domain.auth.request.LoginRequest;
import com.cowave.sys.admin.domain.auth.request.RegisterRequest;
import com.cowave.sys.admin.domain.base.SysAttach;
import com.cowave.sys.admin.service.auth.AuthService;
import com.cowave.sys.admin.service.auth.CaptchaService;
import com.cowave.sys.admin.domain.base.dto.SysLogDto;
import com.cowave.sys.admin.domain.base.vo.OnlineUser;
import com.cowave.sys.admin.domain.rabc.vo.Route;
import com.cowave.sys.admin.service.auth.LdapService;
import com.cowave.sys.admin.service.base.SysAttachService;
import com.cowave.sys.admin.service.base.SysLogService;
import com.cowave.sys.admin.domain.auth.OAuthUser;
import com.cowave.sys.admin.service.auth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.cowave.sys.admin.domain.rabc.enums.UserType.OAUTH_GITLAB;
import static com.cowave.sys.admin.domain.rabc.enums.UserType.SYS;

/**
 * 鉴权
 * @order 8
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final ApplicationProperties applicationProperties;
    private final RedisHelper redisHelper;
    private final SysAttachService attachService;
    private final CaptchaService captchaService;
    private final SysLogService sysLogService;
    private final AuthService authService;
    private final LdapService ldapService;
    private final OAuthService oauthService;

    /**
     * 验证码
     */
    @GetMapping("/captcha")
    public Response<CaptchaInfo> captcha() throws IOException {
        return Response.success(captchaService.captcha());
    }

    /**
     * 邮箱验证码
     */
    @GetMapping("/captcha/email")
    public Response<Void> captchaEmail(@NotNull(message = "{admin.user.email.notnull}") String email) throws Exception {
        return Response.success(() -> captchaService.captchaEmail(email));
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Response<String> register(@Validated @RequestBody RegisterRequest request) {
        captchaService.validEmail(request);
        return Response.success(authService.register(request));
    }

    /**
     * 登录
     */
    @PostMapping("/logon")
    public Response<AccessUserDetails> logon(@Validated @RequestBody LoginRequest request) {
        return Response.success(authService.login(request.getUserAccount(), request.getPassWord()));
    }

    /**
     * 登录（验证码）
     */
    @PostMapping("/login")
    public Response<AccessUserDetails> login(@Validated @RequestBody LoginRequest request) {
        captchaService.validCaptcha(request);
        return Response.success(authService.login(request.getUserAccount(), request.getPassWord()));
    }

    /**
     * Ldap认证
     */
    @PostMapping("/ldap")
    public Response<AccessUserDetails> ldap(@Validated @RequestBody LdapRequest request) {
        return Response.success(ldapService.authenticate(request.getUserAccount(), request.getPassWord()));
    }

    /**
     * Gitlab回调认证
     */
    @GetMapping("/callback/gitlab")
    public Response<AccessUserDetails> gitlabCallback(@RequestParam("code") String code) {
        return Response.success(oauthService.gitlabCallback(code));
    }

    /**
     * 退出
     */
    @Operation(type = "admin_login", action = "logout", desc = "退出登录")
    @RequestMapping("/logout")
    public Response<Void> logout(HttpServletResponse response) throws Exception {
        return Response.success(() -> authService.logout(response));
    }

    /**
     * 令牌刷新
     */
    @RequestMapping("/refresh")
    public Response<AccessUserDetails> refresh(@NotNull(message = "{admin.refreshToken.notnull}") String refreshToken) throws Exception {
        return Response.success(authService.refresh(refreshToken));
    }

    /**
     * 登录信息
     */
    @GetMapping("/info")
    public Response<AuthInfo> info() throws Exception {
        AccessUserDetails userDetails = Access.userDetails();
        Integer userId = userDetails.getUserId();

        AuthInfo authInfo = new AuthInfo();
        authInfo.setUserId(userId);
        authInfo.setUserName(userDetails.getUserNick());
        authInfo.setRoles(userDetails.getRoles());
        authInfo.setPermissions(userDetails.getPermissions());

        if(OAUTH_GITLAB.isEqual(userDetails.getType())){
            OAuthUser oAuthUser = oauthService.infoUser(userId);
            authInfo.setUserEmail(oAuthUser.getUserEmail());
            authInfo.setAvatar(oAuthUser.getUserAvatar());
        }else if(SYS.isEqual(userDetails.getType())){
            SysAttach avatar = attachService.latestOfMaster(Long.valueOf(userId), "sys-user");
            if(avatar != null){
                authInfo.setAvatar(avatar.getViewUrl());
            }
        }
        return Response.success(authInfo);
    }

    /**
     * 路由信息
     */
    @GetMapping("/routes")
    public Response<List<Route>> routes() {
        return Response.success(authService.routes());
    }

    /**
     * 在线用户
     */
    @PostMapping("/online")
    public Response<Response.Page<OnlineUser>> onlineList(@RequestBody OnlineUser login) {
        List<OnlineUser> list = new ArrayList<>();
        for (String key : redisHelper.keys("sys:token:*")) {
            AccessUserDetails userDetails = redisHelper.getValue(key);
            if(userDetails == null
                    || (login.getUserAccount() != null && !userDetails.getUsername().contains(login.getUserAccount()))
                    || (login.getBeginTime() != null && login.getBeginTime().after(userDetails.getAccessTime()))
                    || (login.getEndTime() != null && login.getEndTime().before(userDetails.getAccessTime()))){
                continue;
            }
            list.add(new OnlineUser(userDetails));
        }
        return Response.page(list);
    }

    /**
     * 强退用户
     */
    @PreAuthorize("@permit.hasPermit('monitor:online:force')")
    @GetMapping("/outline/{tokenType}/{userAccount}")
    public Response<String> outline(@PathVariable String tokenType, @PathVariable String userAccount) {
        String tokenKey = applicationProperties.getTokenNamespace() + tokenType + ":" + userAccount;
    	AccessUserDetails userDetails = redisHelper.getValue(tokenKey);
    	if(userDetails == null){
    		return Response.success();
        }
    	redisHelper.delete(tokenKey);
        // 强退日志
        SysLogDto sysLog = new SysLogDto();
        sysLog.initialize();
        sysLog.recordOperation("admin_login", "logout_force", "强退用户：[" + userDetails.getUserNick() + "]");
        sysLogService.add(sysLog);
        return Response.success();
    }
}
