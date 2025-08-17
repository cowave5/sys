package com.cowave.sys.meter.service.torna.impl;

import com.cowave.sys.meter.domain.torna.bean.RequestContext;
import com.cowave.sys.meter.domain.torna.entity.Module;
import com.cowave.sys.meter.domain.torna.param.DocPushParam;
import com.cowave.sys.meter.service.torna.DocService;
import org.springframework.stereotype.Service;

@Service
public class DocServiceImpl implements DocService {

    @Override
    public void doPush(DocPushParam param, RequestContext context) {
        Module module = context.getModule();
        long moduleId = module.getId();
        long startTime = System.currentTimeMillis();

    }
}
