package com.cowave.sys.meter.infra.torna;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.Prop;
import com.cowave.sys.meter.infra.torna.mapper.PropMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PropDao extends ServiceImpl<PropMapper, Prop> {

}
