package com.cowave.sys.meter.open;

import com.cowave.sys.meter.domain.torna.param.EnumInfoCreateBatchParam;
import com.cowave.sys.meter.domain.torna.param.EnumInfoCreateParam;
import com.gitee.easyopen.annotation.Api;
import com.gitee.easyopen.annotation.ApiService;
import com.gitee.easyopen.doc.annotation.ApiDoc;
import com.gitee.easyopen.doc.annotation.ApiDocMethod;

/**
 * @author tanghc
 */
@ApiService
@ApiDoc(value = "字典API", order = 2)
public class EnumApi {

    @Api(name = "enum.push")
    @ApiDocMethod(description = "推送字典", order = 1)
    public void push(EnumInfoCreateParam param) {

    }

    @Api(name = "enum.batch.push")
    @ApiDocMethod(description = "批量推送字典", order = 2)
    public void batchPush(EnumInfoCreateBatchParam param) {

    }
}
