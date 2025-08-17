package com.cowave.sys.meter.open.interceptor;

import com.cowave.commons.tools.ServletUtils;
import com.cowave.sys.meter.domain.torna.bean.ApiUser;
import com.cowave.sys.meter.domain.torna.bean.RequestContext;
import com.gitee.easyopen.ApiContext;
import com.gitee.easyopen.interceptor.ApiInterceptorAdapter;
import com.gitee.easyopen.message.Errors;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tanghc
 */
public class DocInterceptor extends ApiInterceptorAdapter {

    private static final ApiUser DEFAULT_USER = new ApiUser();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object serviceObj, Object argu) throws Exception {
        String apiToken = ApiContext.getAccessToken();
        if (StringUtils.isBlank(apiToken)) {
            throw Errors.ERROR_ACCESS_TOKEN.getException();
        }

//        ModuleService moduleService = SpringContext.getBean(ModuleService.class);
//        Module module = moduleService.getByToken(accessToken);
//        if (module == null) {
//            throw Errors.ERROR_ACCESS_TOKEN.getException();
//        }

        String ip = ServletUtils.getRequestIp(request);
        RequestContext currentContext = RequestContext.getCurrentContext();
        currentContext.setIp(ip);
//        currentContext.setModule(module);
        currentContext.setToken(apiToken);
        currentContext.setApiUser(DEFAULT_USER);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object serviceObj, Object argu, Object result, Exception e) throws Exception {
        RequestContext.getCurrentContext().reset();
    }
}
