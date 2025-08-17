package com.cowave.sys.meter.infra.torna;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.PushIgnoreField;
import com.cowave.sys.meter.infra.torna.mapper.PushIgnoreFieldMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PushIgnoreFieldDao extends ServiceImpl<PushIgnoreFieldMapper, PushIgnoreField> {

}
