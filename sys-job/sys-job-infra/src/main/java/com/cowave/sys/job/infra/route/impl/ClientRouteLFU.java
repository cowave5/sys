package com.cowave.sys.job.infra.route.impl;

import com.cowave.sys.job.domain.client.TriggerRequest;
import com.cowave.sys.job.infra.route.ClientRouter;
import com.cowave.sys.job.infra.ClientService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 最不经常使用，频率/次数
 * @author xuxueli/shanhuiming
 */
public class ClientRouteLFU implements ClientRouter {

    private static final ConcurrentMap<Integer, HashMap<String, Integer>> JOB_LFU_MAP = new ConcurrentHashMap<>();

    private static long CACHE_VALID_TIME = 0;

    @Override
    public String route(ClientService clientService, TriggerRequest triggerParam, List<String> addressList) {
        return route(triggerParam.getTriggerId(), addressList);
    }

    public String route(int jobId, List<String> addressList) {

        // cache clear
        if (System.currentTimeMillis() > CACHE_VALID_TIME) {
            JOB_LFU_MAP.clear();
            CACHE_VALID_TIME = System.currentTimeMillis() + 1000*60*60*24;
        }

        // Key排序可以用TreeMap+构造入参Compare；Value排序暂时只能通过ArrayList
        HashMap<String, Integer> lfuItemMap = JOB_LFU_MAP.get(jobId);
        if (lfuItemMap == null) {
            lfuItemMap = new HashMap<>();
            // 避免重复覆盖
            JOB_LFU_MAP.putIfAbsent(jobId, lfuItemMap);
        }

        // put new
        for (String address: addressList) {
            if (!lfuItemMap.containsKey(address) || lfuItemMap.get(address) >1000000 ) {
                // 初始化时主动Random一次，缓解首次压力
                lfuItemMap.put(address, new Random().nextInt(addressList.size()));
            }
        }
        // remove old
        List<String> delKeys = new ArrayList<>();
        for (String existKey: lfuItemMap.keySet()) {
            if (!addressList.contains(existKey)) {
                delKeys.add(existKey);
            }
        }
        if (!delKeys.isEmpty()) {
            for (String delKey: delKeys) {
                lfuItemMap.remove(delKey);
            }
        }

        // load least userd count address
        List<Map.Entry<String, Integer>> lfuItemList = new ArrayList<>(lfuItemMap.entrySet());
        lfuItemList.sort(Map.Entry.comparingByValue());

        Map.Entry<String, Integer> addressItem = lfuItemList.get(0);
        addressItem.setValue(addressItem.getValue() + 1);
        return addressItem.getKey();
    }
}
