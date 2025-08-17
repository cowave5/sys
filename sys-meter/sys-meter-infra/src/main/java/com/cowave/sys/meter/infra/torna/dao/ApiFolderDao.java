package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.ApiFolder;
import com.cowave.sys.meter.infra.torna.dao.mapper.ApiFolderMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ApiFolderDao extends ServiceImpl<ApiFolderMapper, ApiFolder> {

    public ApiFolder getByToken(String token) {
        return lambdaQuery().eq(ApiFolder::getToken, token).one();
    }
}
