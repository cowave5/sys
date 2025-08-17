package com.cowave.sys.meter.infra.torna;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.DocInfo;
import com.cowave.sys.meter.infra.torna.mapper.DocInfoMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DocInfoDao extends ServiceImpl<DocInfoMapper, DocInfo> {

}
