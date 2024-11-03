/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.auth.controller;

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.Operation;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.configuration.ApplicationProperties;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.commons.response.Response;
import com.cowave.sys.admin.core.entity.vo.OnlineUser;
import com.cowave.sys.admin.core.entity.vo.Route;
import com.cowave.sys.admin.oauth.entity.OAuthUser;
import com.cowave.sys.admin.auth.service.AuthService;
import com.cowave.sys.admin.auth.service.CaptchaService;
import com.cowave.sys.admin.oauth.service.OAuthService;
import com.cowave.sys.admin.core.service.SysAttachService;
import com.cowave.sys.admin.core.service.SysLogService;
import com.cowave.sys.admin.core.entity.dto.SysAttachDto;
import com.cowave.sys.admin.core.entity.dto.SysLogDto;
import com.cowave.sys.admin.auth.entity.AuthInfo;
import com.cowave.sys.admin.auth.entity.CaptchaInfo;
import com.cowave.sys.admin.auth.entity.LoginUser;
import com.cowave.sys.admin.auth.entity.RegisterUser;
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
    public Response<AccessUserDetails> login(@Validated @RequestBody LoginUser loginUser) {
        captchaService.valid(loginUser);
        return Response.success(authService.login(loginUser.getUserAccount(), loginUser.getPassWord()));
    }

    /**
     * 登录
     */
    @PostMapping("/logon")
    public Response<AccessUserDetails> logon(@Validated @RequestBody LoginUser loginUser) {
        return Response.success(authService.login(loginUser.getUserAccount(), loginUser.getPassWord()));
    }

    /**
     * 退出
     */
    @Operation(type = "admin_login", action = "logout", desc = "退出登录")
    @RequestMapping("/logout")
    public Response<Void> logout(HttpServletResponse response) throws IOException {
    	authService.logout(response);
        return Response.success();
    }

    /**
     * 令牌刷新
     */
    @RequestMapping("/refresh")
    public Response<AccessUserDetails> refresh(@NotNull(message = "{user.notnull.refreshToken}") String refreshToken) throws Exception {
        return Response.success(authService.refresh(refreshToken));
    }

    /**
     * 登录信息
     */
    @GetMapping("/info")
    public Response<AuthInfo> info() throws Exception {
        AccessUserDetails userDetails = Access.userDetails();
        Long userId = userDetails.getUserId(v -> ((Number) v).longValue());
        AuthInfo authInfo = new AuthInfo();
        authInfo.setUserId(userId);
        authInfo.setUserName(userDetails.getUserNick());
        authInfo.setRoles(userDetails.getRoles());
        authInfo.setPermissions(userDetails.getPermissions());

        if(AccessUserDetails.TYPE_OAUTH.equals(userDetails.getType())){
            OAuthUser oAuthUser = oAuthService.infoUser(userId);
            authInfo.setUserEmail(oAuthUser.getUserEmail());
            authInfo.setAvatar(oAuthUser.getUserAvatar());
        }else{
            SysAttachDto avatar = attachService.latestOfMaster(userId, "sys-user");
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
