package com.cowave.sys.job.route.impl;

import com.cowave.sys.job.domain.request.TriggerRequest;
import com.cowave.sys.job.route.ExecutorRouter;
import com.cowave.sys.job.route.ClientService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author xuxueli/shanhuiming
 */
public class ExecutorRouteConsistentHash extends ExecutorRouter {

    @Override
    public String route(ClientService clientService, TriggerRequest triggerParam, List<String> addressList) {
        return hashJob(triggerParam.getTriggerId(), addressList);
    }

    private String hashJob(int jobId, List<String> addressList) {
        // ------A1------A2-------A3------
        // -----------J1------------------
        TreeMap<Long, String> addressRing = new TreeMap<>();
        for (String address: addressList) {
            int VIRTUAL_NODE_NUM = 100;
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                long addressHash = hash("SHARD-" + address + "-NODE-" + i);
                addressRing.put(addressHash, address);
            }
        }

        long jobHash = hash(String.valueOf(jobId));
        SortedMap<Long, String> lastRing = addressRing.tailMap(jobHash);
        if (!lastRing.isEmpty()) {
            return lastRing.get(lastRing.firstKey());
        }
        return addressRing.firstEntry().getValue();
    }

    private long hash(String key) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }

        md5.reset();
        byte[] keyBytes;
        keyBytes = key.getBytes(StandardCharsets.UTF_8);

        md5.update(keyBytes);
        byte[] digest = md5.digest();

        // hash code, Truncate to 32-bits
        long hashCode = ((long) (digest[3] & 0xFF) << 24)
                | ((long) (digest[2] & 0xFF) << 16)
                | ((long) (digest[1] & 0xFF) << 8)
                | (digest[0] & 0xFF);
        return hashCode & 0xffffffffL;
    }
}
