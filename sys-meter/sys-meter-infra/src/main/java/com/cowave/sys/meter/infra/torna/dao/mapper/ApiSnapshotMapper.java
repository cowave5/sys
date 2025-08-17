package com.cowave.sys.meter.infra.torna.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowave.sys.meter.domain.torna.entity.ApiSnapshot;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author tanghc
 */
@Mapper
public interface ApiSnapshotMapper extends BaseMapper<ApiSnapshot> {

//    @Select("SELECT id, modifier_name modifierName, modifier_time modifierTime " +
//            "FROM doc_snapshot WHERE doc_id=#{docId} ORDER BY id DESC LIMIT 100")
//    @ResultType(DocSnapshot.class)
//    List<DocSnapshot> listDocSnapshotBaseInfo(long docId);

}
