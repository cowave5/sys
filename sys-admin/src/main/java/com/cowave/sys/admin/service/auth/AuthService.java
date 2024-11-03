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
import com.cowave.commons.framework.access.security.AccessToken;
import com.cowave.commons.framework.access.security.BearerTokenService;
import com.cowave.commons.response.exception.AssertsException;
import com.cowave.sys.admin.core.cache.SysConfigCache;
import com.cowave.sys.admin.core.entity.Route;
import com.cowave.sys.admin.core.entity.RouteMeta;
import com.cowave.sys.admin.core.mapper.ProfileMapper;
import com.cowave.sys.admin.service.sys.SysMenuService;
import com.cowave.sys.model.admin.SysMenu;
import com.cowave.sys.model.admin.auth.RegisterUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class AuthService {

    private final BearerTokenService bearerTokenService;

    private final AuthenticationManager authenticationManager;

    private final SysMenuService sysMenuService;

    private final SysConfigCache sysConfigCache;

    private final ProfileMapper profileMapper;

    private final PasswordEncoder passwordEncoder;

    private final LdapService ldapService;

    /**
     * 注册
     */
    @Transactional(rollbackFor = Exception.class)
    public String register(RegisterUser registerUser) {
        String initPasswd = sysConfigCache.getValue("sys.user.initPassword");
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
    public AccessToken login(String userAccount, String passWord) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAccount, passWord));
        } catch (BadCredentialsException e) {
            throw new AssertsException("{auth.passwd}");
        } catch (InternalAuthenticationServiceException e) {
        	Throwable cause = e.getCause();
            if(cause != null){
                if(cause instanceof UserNotFoundException) {
                    AccessToken accessToken = ldapService.authenticate(userAccount, passWord);
                    if(accessToken == null){
                        throw new AssertsException("{user.notexist}", userAccount);
                    }
                    return accessToken;
                }
                if(cause instanceof AssertsException){
                    throw (AssertsException)cause;
                }
            }
        	throw new AssertsException(e, "{auth.failed}");
        } catch (Exception e) {
            throw new AssertsException(e, "{auth.failed}");
        }
        return (AccessToken) authentication.getPrincipal();
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
    public AccessToken refresh(String refreshJwt) throws Exception{
        return bearerTokenService.dualRefreshToken(refreshJwt);
    }

    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu parent) {
        List<SysMenu> childs = new ArrayList<>();
        for (SysMenu child : list) {
            if (child.getParentId().equals(parent.getMenuId())) {
                childs.add(child);
            }
        }
        return childs;
    }

    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }

    private void recursionFn(List<SysMenu> list, SysMenu menu) {
        List<SysMenu> childList = getChildList(list, menu);
        menu.setChildren(childList);
        for (SysMenu child : childList) {
            if (hasChild(list, child)) {
                recursionFn(list, child);
            }
        }
    }

    /**
     * 路由信息
     */
    public List<Route> routes(){
        List<SysMenu> list;
        if(!Access.userIsAdmin() && AccessToken.TYPE_OAUTH.equals(Access.tokenType())){
            list = sysMenuService.oauthMenus(Access.userId(v -> ((Number)v).longValue()));
        }else{
            list = sysMenuService.list(null, 1, 1, true);
        }

        List<SysMenu> rootMenus = new ArrayList<>();
        for(SysMenu menu : list){
            if (menu.getParentId() == 0) {
                recursionFn(list, menu);
                rootMenus.add(menu);
            }
        }
        return buildRoutes(rootMenus);
    }

    public List<Route> buildRoutes(List<SysMenu> menus){
        List<Route> routes = new LinkedList<>();
        for (SysMenu menu : menus) {
            Route route = new Route();
            route.setHidden("L".equals(menu.getMenuType())); // 链接不展示在菜单
            route.setName(menu.routeName());
            route.setPath(menu.routePath());
            route.setComponent(menu.routeComponent());
            route.setQuery(menu.getMenuParam());
            route.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon(), false, menu.getMenuPath()));

            List<SysMenu> cMenus = menu.getChildren();
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
