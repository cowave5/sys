package com.cowave.sys.job.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author xuxueli/shanhuiming
 */
@Data
public class JobTriggerLog {

	private Long id;

	private int triggerId;

	private String triggerType;

	private int triggerStatus;

	private Date triggerTime;

	private String handlerName;

	private String handlerParam;

	private String clientAddress;

	private String shardingParam;

	private Date handleTime;

	private Long handleCost;

	private String failMsg;
}
