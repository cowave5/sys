package com.cowave.sys.job.service;

import com.cowave.sys.job.domain.request.ClientRegistry;
import com.cowave.sys.job.domain.request.TriggerResponse;

import java.util.List;

/**
 * @author xuxueli/shanhuiming
 */
public interface JobClientService {

    void registry(ClientRegistry clientRegistry);

    void unRegistry(ClientRegistry clientRegistry);

    void callback(List<TriggerResponse> list);
}
