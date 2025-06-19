package com.cowave.sys.job.route;

import com.cowave.sys.job.domain.request.TriggerRequest;

import java.util.List;

/**
 * @author xuxueli/shanhuiming
 */
public abstract class ExecutorRouter {

    public abstract String route(ClientService clientService, TriggerRequest triggerParam, List<String> addressList);
}
