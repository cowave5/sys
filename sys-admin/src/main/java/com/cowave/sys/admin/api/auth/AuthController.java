/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.api.auth;

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.Operation;
import com.cowave.commons.framework.access.security.AccessToken;
import com.cowave.commons.framework.configuration.ApplicationProperties;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.commons.response.Response;
import com.cowave.sys.admin.core.entity.OnlineUser;
import com.cowave.sys.admin.core.entity.Route;
import com.cowave.sys.admin.core.entity.oauth.OAuthUser;
import com.cowave.sys.admin.service.auth.AuthService;
import com.cowave.sys.admin.service.auth.CaptchaService;
import com.cowave.sys.admin.service.auth.OAuthService;
import com.cowave.sys.admin.service.sys.SysAttachService;
import com.cowave.sys.admin.service.sys.SysLogService;
import com.cowave.sys.model.admin.SysAttach;
import com.cowave.sys.model.admin.SysLog;
import com.cowave.sys.model.admin.auth.AuthProfile;
import com.cowave.sys.model.admin.auth.CaptchaInfo;
import com.cowave.sys.model.admin.auth.LoginUser;
import com.cowave.sys.model.admin.auth.RegisterUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 鉴权
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    private final RedisHelper redisHelper;

    private final SysAttachService attachService;

    private final CaptchaService captchaService;

    private final OAuthService oAuthService;

    private final SysLogService sysLogService;

    private final ApplicationProperties applicationProperties;

    /**
     * 获取验证码
     */
    @GetMapping("/captcha")
    public Response<CaptchaInfo> captcha() throws IOException {
        return Response.success(captchaService.captcha());
    }

    /**
     * 获取邮箱验证码
     */
    @GetMapping("/captcha/email")
    public Response<Void> captchaEmail(String email) {
        captchaService.captchaEmail(email);
        return Response.success();
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Response<String> register(@Validated @RequestBody RegisterUser registerUser) {
        captchaService.validEmail(registerUser);
        return Response.success(authService.register(registerUser));
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Response<AccessToken> login(@Validated @RequestBody LoginUser loginUser) {
        captchaService.valid(loginUser);
        return Response.success(authService.login(loginUser.getUserAccount(), loginUser.getPassWord()));
    }

    /**
     * 登录
     */
    @PostMapping("/logon")
    public Response<AccessToken> logon(@Validated @RequestBody LoginUser loginUser) {
        return Response.success(authService.login(loginUser.getUserAccount(), loginUser.getPassWord()));
    }

    /**
     * 退出
     */
    @Operation(type = "admin_login", action = "logout", desc = "退出登录", handleExpr = "#opHandler.log(#opInfo)")
    @RequestMapping("/logout")
    public Response<Void> logout(HttpServletResponse response) throws IOException {
    	authService.logout(response);
        return Response.success();
    }

    /**
     * 令牌刷新
     */
    @RequestMapping("/refresh")
    public Response<AccessToken> refresh(@NotNull(message = "{user.notnull.refreshToken}") String refreshToken) throws Exception {
        return Response.success(authService.refresh(refreshToken));
    }

    /**
     * 登录信息
     */
    @GetMapping("/info")
    public Response<AuthProfile> info() throws Exception {
        AccessToken accessToken = Access.token();
        Long userId = accessToken.getUserId(v -> ((Number) v).longValue());
        AuthProfile authProfile = new AuthProfile();
        authProfile.setUserId(userId);
        authProfile.setUserName(accessToken.getUserNick());
        authProfile.setRoles(accessToken.getRoles());
        authProfile.setPermissions(accessToken.getPermissions());
        if(AccessToken.TYPE_OAUTH.equals(accessToken.getType())){
            OAuthUser oAuthUser = oAuthService.infoUser(userId);
            authProfile.setUserEmail(oAuthUser.getUserEmail());
            authProfile.setAvatar(oAuthUser.getUserAvatar());
        }else{
            SysAttach avatar = attachService.latestOfMaster(userId, "sys-user");
            if(avatar != null){
                authProfile.setAvatar(avatar.getViewUrl());
            }
        }
        return Response.success(authProfile);
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
            AccessToken accessToken = redisHelper.getValue(key);
            if(accessToken == null
                    || (login.getUserAccount() != null && !accessToken.getUsername().contains(login.getUserAccount()))
                    || (login.getBeginTime() != null && login.getBeginTime().after(accessToken.getAccessTime()))
                    || (login.getEndTime() != null && login.getEndTime().before(accessToken.getAccessTime()))){
                continue;
            }
            list.add(new OnlineUser(accessToken));
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
    	AccessToken accessToken = redisHelper.getValue(tokenKey);
    	if(accessToken == null){
    		return Response.success();
        }
    	redisHelper.delete(tokenKey);
        // 强退日志
        SysLog sysLog = new SysLog();
        sysLog.initialize();
        sysLog.recordOperation("admin_login", "logout_force", "强退用户：[" + accessToken.getUserNick() + "]");
        sysLogService.add(sysLog);
        return Response.success();
    }
}
