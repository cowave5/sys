package com.cowave.sys.admin.infra.auth.dao;

import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.BearerTokenService;
import com.cowave.commons.framework.configuration.ApplicationProperties;
import com.cowave.sys.admin.domain.rabc.SysDept;
import com.cowave.sys.admin.domain.rabc.SysTenant;
import com.cowave.sys.admin.domain.rabc.SysUser;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysDeptDtoMapper;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysMenuDtoMapper;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysRoleDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cowave.commons.framework.access.security.Permission.PERMIT_ADMIN;
import static com.cowave.commons.framework.access.security.Permission.ROLE_ADMIN;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class UserDetailsDao {
    private final ApplicationProperties applicationProperties;
    private final BearerTokenService bearerTokenService;
    private final SysDeptDtoMapper sysDeptDtoMapper;
    private final SysRoleDtoMapper sysRoleDtoMapper;
    private final SysMenuDtoMapper sysMenuDtoMapper;

    public AccessUserDetails newUserDetails(String authType, SysTenant sysTenant, SysUser sysUser) {
        AccessUserDetails userDetails = AccessUserDetails.newUserDetails();
        userDetails.setAuthType(authType);
        // 用户信息
        userDetails.setUserType(sysUser.getUserType().getVal());
        userDetails.setTenantId(sysUser.getTenantId());
        userDetails.setUserId(sysUser.getUserId());
        userDetails.setUserCode(sysUser.getUserCode());
        userDetails.setUsername(sysUser.getUserAccount());
        userDetails.setUserNick(sysUser.getUserName());
        userDetails.setUserPasswd(sysUser.getUserPasswd());
        // 部门信息
        SysDept userDept = sysDeptDtoMapper.getPrimaryDeptByUserId(sysUser.getUserId());
        if (userDept != null) {
            userDetails.setDeptId(userDept.getDeptId());
            userDetails.setDeptCode(userDept.getDeptCode());
            userDetails.setDeptName(userDept.getDeptName());
        }
        // 角色信息
        List<String> roleCodes = sysRoleDtoMapper.getRoleCodesByUserId(sysUser.getUserId());
        userDetails.setRoles(roleCodes);
        // 权限信息
        if(roleCodes.contains(ROLE_ADMIN)){
            userDetails.setPermissions(List.of(PERMIT_ADMIN));
        }else{
            List<String> permitCodes = sysMenuDtoMapper.listPermitsByUserId(sysUser.getTenantId(), sysUser.getUserId());
            userDetails.setPermissions(permitCodes);
        }
        // 集群信息
        userDetails.setClusterId(applicationProperties.getClusterId());
        userDetails.setClusterLevel(applicationProperties.getClusterLevel());
        userDetails.setClusterName(applicationProperties.getClusterName());
        bearerTokenService.assignAccessRefreshToken(userDetails);
        // 租户首页
        userDetails.setTenantIndex(sysTenant.getViewIndex());
        return userDetails;
    }
}
