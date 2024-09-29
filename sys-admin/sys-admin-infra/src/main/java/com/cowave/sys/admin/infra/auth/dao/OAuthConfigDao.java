package com.cowave.sys.admin.infra.auth.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.admin.domain.auth.OAuthConfig;
import com.cowave.sys.admin.infra.auth.dao.mapper.OAuthConfigMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class OAuthConfigDao extends ServiceImpl<OAuthConfigMapper, OAuthConfig> {

    public List<OAuthConfig> queryEnabledList(){
        return lambdaQuery().eq(OAuthConfig::getStatus, 1).list();
    }

    public OAuthConfig queryByAppType(String appType){
        return lambdaQuery().eq(OAuthConfig::getAppType, appType).one();
    }

    public void removeByAppType(String appType){
        lambdaUpdate().eq(OAuthConfig::getAppType, appType).remove();
    }
}
