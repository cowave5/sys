package com.cowave.sys.meter.infra.torna;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.DocParam;
import com.cowave.sys.meter.infra.torna.mapper.DocParamMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DocParamDao extends ServiceImpl<DocParamMapper, DocParam> {

}
