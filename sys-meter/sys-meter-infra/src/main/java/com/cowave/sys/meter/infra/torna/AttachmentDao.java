package com.cowave.sys.meter.infra.torna;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.Attachment;
import com.cowave.sys.meter.infra.torna.mapper.AttachmentMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AttachmentDao extends ServiceImpl<AttachmentMapper, Attachment> {

}
