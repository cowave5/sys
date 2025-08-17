package com.cowave.sys.meter.infra.torna;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.DocComment;
import com.cowave.sys.meter.infra.torna.mapper.DocCommentMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DocCommentDao extends ServiceImpl<DocCommentMapper, DocComment> {

}
