package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.ApiEnum;
import com.cowave.sys.meter.infra.torna.dao.mapper.ApiEnumMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ApiEnumDao extends ServiceImpl<ApiEnumMapper, ApiEnum> {

    public ApiEnum getByDataId(String dataId) {
        return lambdaQuery().eq(ApiEnum::getDataId, dataId).one();
    }
}
