package com.cowave.sys.job.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author xuxueli/shanhuiming
 */
@Data
public class JobTrigger {

	@TableId(type = IdType.AUTO)
	private Integer id;

	private int status;

	private String triggerType;

	private String triggerParam;

	private String handlerName;

	private String handlerParam;

	private String misfireStrategy;

	private String routeStrategy;

	private String blockStrategy;

	private int timeout;

	private String glueType;

	private String glueSource;

	private String glueRemark;

	private long glueUpdateTime;;

	private long lastTime;

	private long nextTime;

	private String jobDesc;

	private String alarmEmail;

	private String createBy;

	private Date createTime;

	private Date updateTime;
}
