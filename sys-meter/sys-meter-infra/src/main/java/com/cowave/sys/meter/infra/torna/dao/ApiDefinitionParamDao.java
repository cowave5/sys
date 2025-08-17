package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.ApiDefinitionParam;
import com.cowave.sys.meter.domain.torna.enums.OperationMode;
import com.cowave.sys.meter.infra.torna.dao.mapper.ApiDefinitionParamMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApiDefinitionParamDao extends ServiceImpl<ApiDefinitionParamMapper, ApiDefinitionParam> {

    public void deletePushParam(List<Long> docIdList) {
        lambdaUpdate()
                .in(ApiDefinitionParam::getApiId, docIdList)
                .eq(ApiDefinitionParam::getCreateMode, OperationMode.OPEN.getType())
                .remove();
    }

    public ApiDefinitionParam getByDataId(String dataId) {
        return lambdaQuery().eq(ApiDefinitionParam::getDataId, dataId).one();
    }

    public List<ApiDefinitionParam> listByDocId(Long docId) {
        return lambdaQuery().eq(ApiDefinitionParam::getApiId, docId).list();
    }
}
