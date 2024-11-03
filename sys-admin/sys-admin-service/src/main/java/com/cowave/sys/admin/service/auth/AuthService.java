/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.auth;

import cn.hutool.crypto.digest.DigestUtil;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.BearerTokenService;
import com.cowave.sys.admin.infra.auth.UserNotFoundException;
import com.cowave.sys.admin.domain.auth.RegisterUser;
import com.cowave.sys.admin.infra.auth.mapper.ProfileMapper;
import com.cowave.sys.admin.domain.rabc.dto.SysMenuDto;
import com.cowave.sys.admin.domain.rabc.vo.Route;
import com.cowave.sys.admin.domain.rabc.vo.RouteMeta;
import com.cowave.sys.admin.infra.base.redis.SysConfigRedis;
import com.cowave.sys.admin.service.rabc.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class AuthService {

    private final BearerTokenService bearerTokenService;

    private final AuthenticationManager authenticationManager;

    private final SysMenuService sysMenuService;

    private final SysConfigRedis sysConfigRedis;

    private final ProfileMapper profileMapper;

    private final PasswordEncoder passwordEncoder;

    private final LdapService ldapService;

    /**
     * 注册
     */
    @Transactional(rollbackFor = Exception.class)
    public String register(RegisterUser registerUser) {
        String initPasswd = sysConfigRedis.getConfig("sys.user.initPassword");
        if(StringUtils.isBlank(initPasswd)){
            initPasswd = "123456";
        }
        registerUser.setUserPasswd(passwordEncoder.encode(initPasswd));
        registerUser.setUserCode(DigestUtil.md5Hex(registerUser.getUserAccount()));
        profileMapper.register(registerUser);
        profileMapper.initRole(registerUser.getUserId());
        profileMapper.initHistoryNotice(registerUser.getUserId());
        profileMapper.updateNoticeStat();
        return initPasswd;
    }

    /**
     * 登录
     */
    public AccessUserDetails login(String userAccount, String passWord) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAccount, passWord));
        } catch (InternalAuthenticationServiceException e) {
            Throwable cause = e.getCause();
            if (cause instanceof UserNotFoundException) {
                AccessUserDetails userDetails = ldapService.authenticate(userAccount, passWord);
                if (userDetails != null) {
                    return userDetails;
                }
            }
            throw e;
        }
        return (AccessUserDetails) authentication.getPrincipal();
    }

    /**
     * 退出
     */
    public void logout(HttpServletResponse response) throws IOException {
    	 String token = Access.accessToken();
    	 if(token != null) {
    		 bearerTokenService.dualRemoveToken(response, token);
    	 }
    }

    /**
     * 刷新令牌
     */
    public AccessUserDetails refresh(String refreshJwt) throws Exception{
        return bearerTokenService.dualRefreshToken(refreshJwt);
    }

    private List<SysMenuDto> getChildList(List<SysMenuDto> list, SysMenuDto parent) {
        List<SysMenuDto> childs = new ArrayList<>();
        for (SysMenuDto child : list) {
            if (child.getParentId().equals(parent.getMenuId())) {
                childs.add(child);
            }
        }
        return childs;
    }

    private boolean hasChild(List<SysMenuDto> list, SysMenuDto t) {
        return getChildList(list, t).size() > 0;
    }

    private void recursionFn(List<SysMenuDto> list, SysMenuDto menu) {
        List<SysMenuDto> childList = getChildList(list, menu);
        menu.setChildren(childList);
        for (SysMenuDto child : childList) {
            if (hasChild(list, child)) {
                recursionFn(list, child);
            }
        }
    }

    /**
     * 路由信息
     */
    public List<Route> routes(){
        List<SysMenuDto> list;
        if(!Access.userIsAdmin() && AccessUserDetails.TYPE_OAUTH.equals(Access.tokenType())){
            list = sysMenuService.oauthMenus(Access.userId());
        }else{
            list = sysMenuService.list(null, 1, 1, true);
        }

        List<SysMenuDto> rootMenus = new ArrayList<>();
        for(SysMenuDto menu : list){
            if (menu.getParentId() == 0) {
                recursionFn(list, menu);
                rootMenus.add(menu);
            }
        }
        return buildRoutes(rootMenus);
    }

    public List<Route> buildRoutes(List<SysMenuDto> menus){
        List<Route> routes = new LinkedList<>();
        for (SysMenuDto menu : menus) {
            Route route = new Route();
            route.setHidden("L".equals(menu.getMenuType())); // 链接不展示在菜单
            route.setName(menu.routeName());
            route.setPath(menu.routePath());
            route.setComponent(menu.routeComponent());
            route.setQuery(menu.getMenuParam());
            route.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon(), false, menu.getMenuPath()));

            List<SysMenuDto> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && "M".equals(menu.getMenuType())) {
                route.setAlwaysShow(true);
                route.setRedirect("noRedirect");
                route.setChildren(buildRoutes(cMenus));
            } else if (menu.ifMenuFrame()) {
                route.setMeta(null);
                List<Route> childrenList = new ArrayList<>();
                Route children = new Route();
                children.setPath(menu.getMenuPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getMenuPath()));
                children.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon(), false, menu.getMenuPath()));
                children.setQuery(menu.getMenuParam());
                childrenList.add(children);
                route.setChildren(childrenList);
            } else if (menu.getParentId() == 0L && menu.ifInnerLink()) {
                route.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon()));
                route.setPath("/");
                List<Route> childrenList = new ArrayList<>();
                Route children = new Route();
                String routerPath = menu.getMenuPath();
                routerPath = routerPath.replace("http://", "");
                routerPath = routerPath.replace("https://", "");
                children.setPath(routerPath);
                children.setComponent("InnerLink");
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon(), menu.getMenuPath()));
                childrenList.add(children);
                route.setChildren(childrenList);
            }
            routes.add(route);
        }
        return routes;
    }
}
