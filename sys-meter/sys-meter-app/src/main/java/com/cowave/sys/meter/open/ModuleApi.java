package com.cowave.sys.meter.open;

import com.cowave.sys.meter.domain.torna.param.DebugEnvDeleteParam;
import com.cowave.sys.meter.domain.torna.param.DebugEnvParam;
import com.cowave.sys.meter.domain.torna.result.ModuleResult;
import com.gitee.easyopen.annotation.Api;
import com.gitee.easyopen.annotation.ApiService;
import com.gitee.easyopen.doc.annotation.ApiDoc;
import com.gitee.easyopen.doc.annotation.ApiDocMethod;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tanghc
 */
@Slf4j
@ApiService
@ApiDoc(value = "模块API", order = 3)
public class ModuleApi {

    @Api(name = "module.debug.env.set")
    @ApiDocMethod(description = "设置调试环境", order = 1, remark = "新增或修改调试环境")
    public void setDebugEnv(DebugEnvParam param) {
        log.info("--------------------");

    }

    @Api(name = "module.debug.env.delete")
    @ApiDocMethod(description = "删除调试环境", order = 2)
    public void delDebugEnv(DebugEnvDeleteParam param) {
        log.info("--------------------");

    }

    @Api(name = "module.get")
    @ApiDocMethod(description = "获取应用信息", order = 3)
    public ModuleResult getModuleInfo() {
        log.info("--------------------");

        return null;
    }
}
