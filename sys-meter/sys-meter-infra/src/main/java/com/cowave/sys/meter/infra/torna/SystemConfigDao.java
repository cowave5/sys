package com.cowave.sys.meter.infra.torna;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.SystemConfig;
import com.cowave.sys.meter.infra.torna.mapper.SystemConfigMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SystemConfigDao extends ServiceImpl<SystemConfigMapper, SystemConfig> {

}
