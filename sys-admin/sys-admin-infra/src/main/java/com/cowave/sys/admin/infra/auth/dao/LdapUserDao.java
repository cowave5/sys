package com.cowave.sys.admin.infra.auth.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.domain.auth.LdapUser;
import com.cowave.sys.admin.domain.auth.OAuthUser;
import com.cowave.sys.admin.infra.auth.dao.mapper.LdapUserMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shanhuiming
 */
@Repository
public class LdapUserDao extends ServiceImpl<LdapUserMapper, LdapUser> {

    public String getRoleCodeByAccount(String userAccount){
        return lambdaQuery()
                .eq(LdapUser::getUserAccount, userAccount)
                .oneOpt().map(LdapUser::getRoleCode).orElse("user-readonly");
    }

    public void updateRoleCodeById(Integer userId, String roleCode){
        lambdaUpdate().eq(LdapUser::getId, userId)
                .set(LdapUser::getRoleCode, roleCode)
                .set(LdapUser::getUpdateTime, new Date())
                .update();
    }

    public String queryNameByCode(String userCode){
        return lambdaQuery()
                .eq(LdapUser::getUserCode, userCode)
                .select(LdapUser::getUserName)
                .oneOpt().map(LdapUser::getUserName).orElse(null);
    }

    public Map<String, String> queryCodeNameMap(List<String> userCodes){
        if(userCodes.isEmpty()){
            return new HashMap<>();
        }

        List<LdapUser> list = lambdaQuery()
                .in(LdapUser::getUserCode, userCodes)
                .select(LdapUser::getUserCode, LdapUser::getUserName)
                .list();
        return Collections.copyToMap(list, LdapUser::getUserCode, LdapUser::getUserName);
    }
}
