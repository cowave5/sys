package com.cowave.sys.job.route.impl;

import com.cowave.commons.client.http.constants.HttpCode;
import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.job.domain.request.TriggerRequest;
import com.cowave.sys.job.route.ExecutorRouter;
import com.cowave.sys.job.route.ClientService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * @author xuxueli/shanhuiming
 */
@Slf4j
public class ExecutorRouteFailover extends ExecutorRouter {

    @Override
    public String route(ClientService clientService, TriggerRequest triggerParam, List<String> addressList) {
        for (String address : addressList) {
            try {
                Response<String> response = clientService.beat(address);
                if (Objects.equals(response.getCode(), HttpCode.SUCCESS.getCode())) {
                    return address;
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return null;
    }
}
