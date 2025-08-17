package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.ApiSnapshot;
import com.cowave.sys.meter.infra.torna.dao.mapper.ApiSnapshotMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApiSnapshotDao extends ServiceImpl<ApiSnapshotMapper, ApiSnapshot> {

    public ApiSnapshot getByMd5(String md5) {
        return lambdaQuery().eq(ApiSnapshot::getMd5, md5).one();
    }

    public List<ApiSnapshot> listByDocId(Long docId) {
        return lambdaQuery()
                .eq(ApiSnapshot::getApiId, docId)
                .orderByAsc(ApiSnapshot::getId)
                .list();
    }
}
