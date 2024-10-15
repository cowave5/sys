/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.api.service.auth;

import java.util.List;

import com.cowave.commons.framework.filter.security.AccessToken;
import com.cowave.commons.framework.filter.security.Permission;
import com.cowave.commons.framework.filter.security.TokenService;
import com.cowave.commons.tools.Asserts;
import com.cowave.sys.admin.api.service.SysLogService;
import com.cowave.sys.model.admin.SysLog;
import com.cowave.sys.model.admin.SysUserRole;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cowave.sys.admin.api.mapper.SysUserMapper;
import com.cowave.commons.framework.configuration.ClusterInfo;
import com.cowave.sys.model.admin.SysUser;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final TokenService tokenService;

    private final SysUserMapper sysUserMapper;

    private final ClusterInfo clusterInfo;

    private final SysLogService sysLogService;

    @Override
	public UserDetails loadUserByUsername(String userAccount) throws UsernameNotFoundException {
    	SysUser sysUser = sysUserMapper.queryByAccount(userAccount);
        if(sysUser == null){
            throw new UserNotFoundException("{user.notexist}");
        }
        Asserts.equals(1, sysUser.getUserStatus(), "{auth.account.disable}", userAccount);
        // 获取用户角色权限
        List<String> roleCodes = sysUser.getRoleList().stream().map(SysUserRole::getRoleCode).toList();
        List<String> permits;
        if(roleCodes.contains(Permission.ROLE_ADMIN)){
            permits = List.of(Permission.PERMIT_ADMIN);
        }else{
            permits = sysUserMapper.permitKeys(sysUser.getUserId());
        }
        // 构造token
        AccessToken accessToken = SysUser.accessToken(sysUser);
        accessToken.setRoles(roleCodes);
        accessToken.setPermissions(permits);
        accessToken.setClusterId(clusterInfo.getId());
        accessToken.setClusterName(clusterInfo.getName());
        accessToken.setClusterLevel(clusterInfo.getLevel());
        tokenService.assignToken(accessToken);
        // 登录日志
        SysLog sysLog = new SysLog();
        sysLog.initialize();
        sysLog.recordOperation("admin_login", "login", "登录");
        sysLogService.add(sysLog);
        // 返回
        return accessToken;
	}
}
