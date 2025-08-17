package com.cowave.sys.meter.infra.torna;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.SystemLoginToken;
import com.cowave.sys.meter.infra.torna.mapper.SystemLoginTokenMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SystemLoginTokenDao extends ServiceImpl<SystemLoginTokenMapper, SystemLoginToken> {

}
