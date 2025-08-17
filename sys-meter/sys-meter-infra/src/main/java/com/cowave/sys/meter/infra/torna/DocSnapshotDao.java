package com.cowave.sys.meter.infra.torna;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.DocSnapshot;
import com.cowave.sys.meter.infra.torna.mapper.DocSnapshotMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DocSnapshotDao extends ServiceImpl<DocSnapshotMapper, DocSnapshot> {

}
