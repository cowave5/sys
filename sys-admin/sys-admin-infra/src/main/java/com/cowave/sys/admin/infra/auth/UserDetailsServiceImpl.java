/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.auth;

import com.cowave.commons.client.http.asserts.Asserts;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.BearerTokenService;
import com.cowave.commons.framework.access.security.Permission;
import com.cowave.sys.admin.infra.base.dao.mapper.dto.SysLogDtoMapper;
import com.cowave.sys.admin.infra.rabc.dao.SysUserDao;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysDeptDtoMapper;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysMenuDtoMapper;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysRoleDtoMapper;
import com.cowave.sys.admin.domain.rabc.SysDept;
import com.cowave.sys.admin.domain.rabc.SysUser;
import com.cowave.sys.admin.domain.base.dto.SysLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private final BearerTokenService bearerTokenService;
    private final SysUserDao sysUserDao;
    private final SysLogDtoMapper sysLogMapper;
    private final SysDeptDtoMapper sysDeptDtoMapper;
    private final SysRoleDtoMapper sysRoleDtoMapper;
    private final SysMenuDtoMapper sysMenuDtoMapper;

    @Override
	public UserDetails loadUserByUsername(String userAccount) throws UsernameNotFoundException {
        // 用户信息
        SysUser sysUser = sysUserDao.getByUserAccount(userAccount);
        if(sysUser == null){
            throw new UserNotFoundException("{user.notexist}");
        }
        Asserts.equals(1, sysUser.getUserStatus(), "{auth.account.disable}", userAccount);
        // 部门信息
        SysDept userDept = sysDeptDtoMapper.getDefaultDeptOfUser(sysUser.getUserId());
        // 角色信息
        List<String> roleCodes = sysRoleDtoMapper.getUserRoles(sysUser.getUserId());
        // 权限信息
        List<String> permitKeys;
        if(roleCodes.contains(Permission.ROLE_ADMIN)){
            permitKeys = List.of(Permission.PERMIT_ADMIN);
        }else{
            permitKeys = sysMenuDtoMapper.getPermitsByUserId(sysUser.getUserId());
        }
        // 构造token
        AccessUserDetails userDetails = SysUser.newUserDetails(sysUser, userDept);
        userDetails.setRoles(roleCodes);
        userDetails.setPermissions(permitKeys);
        bearerTokenService.dualAssignToken(userDetails);
        // 登录日志
        SysLogDto sysLog = new SysLogDto();
        sysLog.initialize();
        sysLog.recordOperation("admin_login", "login", "登录");
        sysLogMapper.insert(sysLog);
        // 返回
        return userDetails;
	}
}
