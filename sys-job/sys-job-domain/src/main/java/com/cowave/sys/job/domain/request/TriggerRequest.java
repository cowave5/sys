package com.cowave.sys.job.domain.request;

import lombok.Data;


/**
 * @author xuxueli/shanhuiming
 */
@Data
public class TriggerRequest {

    private int triggerId;

    private long logId;

    private String handlerName;

    private String handlerParams;

    private String blockStrategy;

    private int timeOut;

    private String glueType;

    private String glueSource;

    private long glueUpdateTime;

    private int broadIndex;

    private int broadTotal;
}
