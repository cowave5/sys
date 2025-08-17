package com.cowave.sys.meter.infra.torna;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.EnumItem;
import com.cowave.sys.meter.infra.torna.mapper.EnumItemMapper;
import org.springframework.stereotype.Repository;

@Repository
public class EnumItemDao extends ServiceImpl<EnumItemMapper, EnumItem> {

}
