package com.cowave.sys.job.client;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

/**
 * @author xuxueli/shanhuiming
 */
@RequiredArgsConstructor
@Data
public class JobContent {

    private static final InheritableThreadLocal<JobContent> contextHolder = new InheritableThreadLocal<>();

    public static void set(JobContent jobContent){
        contextHolder.set(jobContent);
    }

    public static JobContent get(){
        return contextHolder.get();
    }

    private final long jobId;

    private final String jobParam;

    private final int shardIndex;

    private final int shardTotal;

    private Date handleTime;

    private int handleStatus;

    private long handleCost;

    private String handleMsg;
}
