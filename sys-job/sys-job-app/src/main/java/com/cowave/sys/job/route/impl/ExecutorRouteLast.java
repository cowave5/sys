package com.cowave.sys.job.route.impl;

import com.cowave.sys.job.domain.request.TriggerRequest;
import com.cowave.sys.job.route.ExecutorRouter;
import com.cowave.sys.job.route.ClientService;

import java.util.List;

/**
 * @author xuxueli/shanhuiming
 */
public class ExecutorRouteLast extends ExecutorRouter {

    @Override
    public String route(ClientService clientService, TriggerRequest triggerParam, List<String> addressList) {
        return addressList.get(addressList.size() - 1);
    }
}
