package com.cowave.sys.job.route.impl;

import com.cowave.sys.job.domain.request.TriggerRequest;
import com.cowave.sys.job.route.ExecutorRouter;
import com.cowave.sys.job.route.ClientService;

import java.util.List;
import java.util.Random;

/**
 * @author xuxueli/shanhuiming
 */
public class ExecutorRouteRandom extends ExecutorRouter {

    private static final Random localRandom = new Random();

    @Override
    public String route(ClientService clientService, TriggerRequest triggerParam, List<String> addressList) {
        return addressList.get(localRandom.nextInt(addressList.size()));
    }
}
