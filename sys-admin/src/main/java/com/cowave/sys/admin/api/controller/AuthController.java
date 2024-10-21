/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.api.controller;

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.Operation;
import com.cowave.commons.framework.access.security.AccessToken;
import com.cowave.commons.framework.configuration.ApplicationProperties;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.sys.admin.api.service.OAuthService;
import com.cowave.sys.admin.api.service.SysAttachService;
import com.cowave.sys.admin.api.service.SysLogService;
import com.cowave.sys.admin.api.service.auth.AuthService;
import com.cowave.sys.admin.api.service.auth.captcha.CaptchaInfo;
import com.cowave.sys.admin.api.service.auth.captcha.CaptchaService;
import com.cowave.sys.admin.core.OplogHandler;
import com.cowave.sys.admin.core.entity.OnlineUser;
import com.cowave.sys.admin.core.entity.Route;
import com.cowave.sys.admin.core.entity.UserProfile;
import com.cowave.sys.admin.core.entity.UserRegister;
import com.cowave.sys.admin.core.entity.oauth.OAuthUser;
import com.cowave.sys.model.admin.Login;
import com.cowave.sys.model.admin.SysAttach;
import com.cowave.sys.model.admin.SysLog;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
    public Response<String> register(@Validated @RequestBody UserRegister userRegister) {
        captchaService.validEmail(userRegister);
        return Response.success(authService.register(userRegister));
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Response<AccessToken> login(@Validated @RequestBody Login login) {
        captchaService.valid(login);
        return Response.success(authService.login(login.getUserAccount(), login.getPassWord()));
    }

    /**
     * 登录
     */
    @PostMapping("/logon")
    public Response<AccessToken> logon(@Validated @RequestBody Login login) {
        return Response.success(authService.login(login.getUserAccount(), login.getPassWord()));
    }

    /**
     * 退出
     */
    @Operation(type = "admin_login", action = "logout", handler = OplogHandler.class,
            summary = "退出登录", expr = "#opHandler.log(#opInfo)")
    @RequestMapping("/logout")
    public Response<Void> logout(HttpServletResponse response) throws IOException {
    	authService.logout(response);
        return Response.success();
    }

    /**
     * 令牌刷新
     */
    @RequestMapping("/refresh")
    public void refresh(HttpServletResponse response, String refreshToken) throws Exception {
        authService.refresh(response, refreshToken);
    }

    /**
     * 登录信息
     */
    @GetMapping("/info")
    public Response<UserProfile> info() throws Exception {
        AccessToken accessToken = Access.token();
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(accessToken.getUserId());
        userProfile.setUserName(accessToken.getUserNick());
        userProfile.setRoles(accessToken.getRoles());
        userProfile.setPermissions(accessToken.getPermissions());
        if(AccessToken.TYPE_OAUTH.equals(accessToken.getType())){
            OAuthUser oAuthUser = oAuthService.infoUser(accessToken.getUserId());
            userProfile.setUserEmail(oAuthUser.getUserEmail());
            userProfile.setAvatar(oAuthUser.getUserAvatar());
        }else{
            SysAttach avatar = attachService.latestOfMaster(accessToken.getUserId(), "sys-user");
            if(avatar != null){
                userProfile.setAvatar(avatar.getViewUrl());
            }
        }
        return Response.success(userProfile);
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
        for (String key : redisHelper.keys("Token:*")) {
            AccessToken accessToken = redisHelper.getValue(key);
            if(accessToken == null
                    || (login.getUserAccount() != null && !accessToken.getUsername().contains(login.getUserAccount()))
                    || (login.getBeginTime() != null && login.getBeginTime().after(accessToken.getAccessTime()))
                    || (login.getEndTime() != null && login.getEndTime().before(accessToken.getAccessTime()))){
                continue;
            }

            OnlineUser onlineUser = new OnlineUser();
            onlineUser.setTokenId(accessToken.getId());
            onlineUser.setTokenType(accessToken.getType());
            onlineUser.setAccessIp(accessToken.getAccessIp());
            onlineUser.setAccessTime(accessToken.getAccessTime());
            onlineUser.setUserAccount(accessToken.getUsername());
            onlineUser.setUserName(accessToken.getUserNick());
            onlineUser.setLoginTime(accessToken.getLoginTime());
            onlineUser.setAccessCluster(accessToken.getClusterName());
            list.add(onlineUser);
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
