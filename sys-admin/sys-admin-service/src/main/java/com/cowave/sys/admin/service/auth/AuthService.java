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

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.BearerTokenService;
import com.cowave.sys.admin.domain.auth.request.RegisterRequest;
import com.cowave.sys.admin.domain.rabc.SysMenu;
import com.cowave.sys.admin.domain.rabc.SysUser;
import com.cowave.sys.admin.domain.rabc.SysUserRole;
import com.cowave.sys.admin.domain.rabc.vo.Route;
import com.cowave.sys.admin.domain.rabc.vo.RouteMeta;
import com.cowave.sys.admin.infra.base.dao.mapper.dto.SysNoticeDtoMapper;
import com.cowave.sys.admin.infra.base.redis.SysConfigRedis;
import com.cowave.sys.admin.infra.rabc.dao.SysRoleDao;
import com.cowave.sys.admin.infra.rabc.dao.SysUserDao;
import com.cowave.sys.admin.infra.rabc.dao.SysUserRoleDao;
import com.cowave.sys.admin.service.rabc.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.cowave.sys.admin.domain.rabc.enums.UserType.SYS;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final BearerTokenService bearerTokenService;
    private final SysMenuService sysMenuService;
    private final SysConfigRedis sysConfigRedis;
    private final SysUserDao sysUserDao;
    private final SysRoleDao sysRoleDao;
    private final SysUserRoleDao sysUserRoleDao;
    private final SysNoticeDtoMapper sysNoticeDtoMapper;

    /**
     * 注册
     */
    @Transactional(rollbackFor = Exception.class)
    public String register(RegisterRequest request) {
        String initPasswd = sysConfigRedis.getConfigValue("sys.user.initPassword");
        if(StringUtils.isBlank(initPasswd)){
            initPasswd = "123456";
        }

        SysUser sysUser = SysUser.builder()
                .userStatus(1)
                .userCode(SYS.generateCode())
                .userEmail(request.getUserEmail())
                .userName(request.getUserName())
                .userAccount(request.getUserAccount())
                .userPasswd(passwordEncoder.encode(initPasswd))
                .build();
        sysUserDao.save(sysUser);

        Integer readOnlyRoleId = sysRoleDao.queryIdByCode("user-readonly");
        sysUserRoleDao.save(new SysUserRole(sysUser.getUserId(), readOnlyRoleId));

        // 注册用户的通知消息
        sysNoticeDtoMapper.initNoticeMsgForNewUser(sysUser.getUserId());
        sysNoticeDtoMapper.updateNoticeStatForNewUser();
        return initPasswd;
    }

    /**
     * 登录
     */
    public AccessUserDetails login(String userAccount, String passwd) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAccount, passwd));
        return (AccessUserDetails) authentication.getPrincipal();
    }

    /**
     * 退出
     */
    public void logout(HttpServletResponse response) throws IOException {
        bearerTokenService.dualRemoveToken(response, Access.accessToken());
    }

    /**
     * 刷新令牌
     */
    public AccessUserDetails refresh(String refreshJwt) throws Exception{
        return bearerTokenService.dualRefreshToken(refreshJwt);
    }

    /**
     * 路由信息
     */
    public List<Route> routes(){
        List<String> roleList = Access.userRoles();
        if(roleList.isEmpty()){
            return Collections.emptyList();
        }

        List<SysMenu> menuList;
        // 系统管理员
        if(roleList.contains("sysAdmin")){
            menuList = sysMenuService.list(null, 1, 1);
        }else{
            // 非系统管理员
            menuList = sysMenuService.authMenus(roleList);
        }

        if(menuList.isEmpty()){
            return Collections.emptyList();
        }

        List<SysMenu> rootMenus = new ArrayList<>();
        for(SysMenu menu : menuList){
            if (menu.getParentId() == 0) {
                recursionFn(menuList, menu);
                rootMenus.add(menu);
            }
        }
        return buildRoutes(rootMenus);
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

    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }

    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu parent) {
        List<SysMenu> children = new ArrayList<>();
        for (SysMenu child : list) {
            if (child.getParentId().equals(parent.getMenuId())) {
                children.add(child);
            }
        }
        return children;
    }

    private List<Route> buildRoutes(List<SysMenu> menus){
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
