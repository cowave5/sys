package com.cowave.sys.meter.infra.torna;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.GenTemplate;
import com.cowave.sys.meter.infra.torna.mapper.GenTemplateMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GenTemplateDao extends ServiceImpl<GenTemplateMapper, GenTemplate> {

}
