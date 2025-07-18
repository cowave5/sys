package com.cowave.sys.job.domain.enums;

import com.cowave.commons.tools.EnumVal;
import lombok.Getter;

/**
 * @author xuxueli/shanhuiming
 */
@Getter
public enum JobScheduleType implements EnumVal<Void> {

    /**
     * Cron表达式
     */
    CRON,

    /**
     * 固定间隔，相对开始时间
     */
    FIX_RATE,

    /**
     * 固定频率，相对结束时间
     */
    FIX_DELAY
}
