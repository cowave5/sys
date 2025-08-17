package com.cowave.sys.meter.service.torna;

import com.cowave.sys.meter.domain.torna.bean.RequestContext;
import com.cowave.sys.meter.domain.torna.param.DocPushParam;

/**
 * @author shanhuiming
 */
public interface DocService {

    void doPush(DocPushParam param, RequestContext context);
}
