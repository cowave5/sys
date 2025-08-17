package com.cowave.sys.meter.infra.torna;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.Module;
import com.cowave.sys.meter.infra.torna.mapper.ModuleMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ModuleDao extends ServiceImpl<ModuleMapper, Module> {

}
