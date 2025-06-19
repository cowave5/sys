package com.cowave.sys.job.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xuxueli/shanhuiming
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TriggerResponse {

    private long logId;

    private int triggerId;

    private int handleStatus;

    private Date handleTime;

    private long handleCost;

    private String handleMsg;
}
