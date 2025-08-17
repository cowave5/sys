package com.cowave.sys.meter.infra.torna;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.OpenUser;
import com.cowave.sys.meter.infra.torna.mapper.OpenUserMapper;
import org.springframework.stereotype.Repository;

@Repository
public class OpenUserDao extends ServiceImpl<OpenUserMapper, OpenUser> {

}
