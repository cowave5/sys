package com.cowave.sys.admin.infra.auth.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.admin.domain.auth.LdapConfig;
import com.cowave.sys.admin.infra.auth.dao.mapper.LdapConfigMapper;
import org.springframework.stereotype.Repository;

/**
 * @author shanhuiming
 */
@Repository
public class LdapConfigDao extends ServiceImpl<LdapConfigMapper, LdapConfig> {

    public LdapConfig queryByName(String ldapName){
        return lambdaQuery().eq(LdapConfig::getLdapName, ldapName).one();
    }

    public void removeByName(String ldapName){
        lambdaUpdate().eq(LdapConfig::getLdapName, ldapName).remove();
    }
}
