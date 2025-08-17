package com.cowave.sys.meter.infra.torna;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.Space;
import com.cowave.sys.meter.infra.torna.mapper.SpaceMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SpaceDao extends ServiceImpl<SpaceMapper, Space> {

}
