/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.auth;

import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.TenantUserDetailsService;
import com.cowave.sys.admin.domain.constants.UserType;
import com.cowave.sys.admin.domain.rabc.SysTenant;
import com.cowave.sys.admin.infra.auth.MfaConfiguration;
import com.cowave.sys.admin.infra.auth.dao.UserDetailsDao;
import com.cowave.sys.admin.infra.rabc.dao.SysTenantDao;
import com.cowave.sys.admin.infra.rabc.dao.SysUserDao;
import com.cowave.sys.admin.domain.rabc.SysUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.cowave.commons.client.http.constants.HttpCode.FORBIDDEN;
import static com.cowave.sys.admin.domain.constants.EnableStatus.ENABLE;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements TenantUserDetailsService {
    private final SysUserDao sysUserDao;
    private final SysTenantDao sysTenantDao;
    private final UserDetailsDao userDetailsDao;
    private final MfaConfiguration mfaConfiguration;

    @Override
	public UserDetails loadTenantUserByUsername(String tenantId, String userAccount) {
        SysUser sysUser = sysUserDao.getByAccount(tenantId, UserType.SYS, userAccount);
        if(sysUser == null){
            return null;
        }
        HttpAsserts.equals(ENABLE, sysUser.getUserStatus(), FORBIDDEN, "{admin.user.account.disable}", userAccount);

        // 租户
        SysTenant sysTenant = sysTenantDao.getById(sysUser.getTenantId());
        HttpAsserts.equals(ENABLE, sysTenant.getStatus(),
                FORBIDDEN, "{admin.user.tenant.disable}", sysTenant.getTenantName());
        if(sysTenant.getExpireTime() != null){
            HttpAsserts.isTrue(sysTenant.getExpireTime().after(new Date()),
                    FORBIDDEN, "{admin.user.tenant.expired}", sysTenant.getTenantName());
        }

        String mfaKey = sysUser.getMfa();
        if(StringUtils.isBlank(mfaKey)){
            return userDetailsDao.newUserDetails(UserType.SYS.getVal(), sysTenant, sysUser);
        }else{
            String mfaToken = mfaConfiguration.buildMfaToken(tenantId, userAccount);
            AccessUserDetails userDetails = new AccessUserDetails();
            userDetails.setUsername(userAccount);
            userDetails.setUserPasswd(sysUser.getUserPasswd());
            userDetails.setMfaRequired(true);
            userDetails.setAccessToken(mfaToken);
            return userDetails;
        }
	}
}
