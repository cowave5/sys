package com.cowave.sys.meter.infra.torna.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowave.sys.meter.domain.torna.entity.DocSnapshot;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author tanghc
 */
@Mapper
public interface DocSnapshotMapper extends BaseMapper<DocSnapshot> {

//    @Select("SELECT id, modifier_name modifierName, modifier_time modifierTime " +
//            "FROM doc_snapshot WHERE doc_id=#{docId} ORDER BY id DESC LIMIT 100")
//    @ResultType(DocSnapshot.class)
//    List<DocSnapshot> listDocSnapshotBaseInfo(long docId);

}
