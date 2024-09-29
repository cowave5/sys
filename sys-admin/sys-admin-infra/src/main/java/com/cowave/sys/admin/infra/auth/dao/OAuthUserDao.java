package com.cowave.sys.admin.infra.auth.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.domain.auth.LdapUser;
import com.cowave.sys.admin.domain.auth.OAuthUser;
import com.cowave.sys.admin.infra.auth.dao.mapper.OAuthUserMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shanhuiming
 */
@Repository
public class OAuthUserDao extends ServiceImpl<OAuthUserMapper, OAuthUser> {

    public String getRoleCodeByAccount(String appType, String userAccount){
        return lambdaQuery()
                .eq(OAuthUser::getAppType, appType)
                .eq(OAuthUser::getUserAccount, userAccount)
                .oneOpt().map(OAuthUser::getRoleCode).orElse("user-readonly");
    }

    public void updateRoleCodeById(Integer userId, String roleCode){
        lambdaUpdate().eq(OAuthUser::getId, userId)
                .set(OAuthUser::getRoleCode, roleCode)
                .set(OAuthUser::getUpdateTime, new Date())
                .update();
    }

    public String queryNameByCode(String userCode){
        return lambdaQuery()
                .eq(OAuthUser::getUserCode, userCode)
                .select(OAuthUser::getUserName)
                .oneOpt().map(OAuthUser::getUserName).orElse(null);
    }

    public Map<String, String> queryCodeNameMap(List<String> userCodes){
        if(userCodes.isEmpty()){
            return new HashMap<>();
        }

        List<OAuthUser> list = lambdaQuery()
                .in(OAuthUser::getUserCode, userCodes)
                .select(OAuthUser::getUserCode, OAuthUser::getUserName)
                .list();
        return Collections.copyToMap(list, OAuthUser::getUserCode, OAuthUser::getUserName);
    }
}
