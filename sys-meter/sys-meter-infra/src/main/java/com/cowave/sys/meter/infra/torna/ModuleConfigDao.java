package com.cowave.sys.meter.infra.torna;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.ModuleConfig;
import com.cowave.sys.meter.infra.torna.mapper.ModuleConfigMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ModuleConfigDao extends ServiceImpl<ModuleConfigMapper, ModuleConfig> {

}
