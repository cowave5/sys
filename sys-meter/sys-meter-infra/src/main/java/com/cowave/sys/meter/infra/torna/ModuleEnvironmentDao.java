package com.cowave.sys.meter.infra.torna;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.ModuleEnvironment;
import com.cowave.sys.meter.infra.torna.mapper.ModuleEnvironmentMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ModuleEnvironmentDao extends ServiceImpl<ModuleEnvironmentMapper, ModuleEnvironment> {

}
