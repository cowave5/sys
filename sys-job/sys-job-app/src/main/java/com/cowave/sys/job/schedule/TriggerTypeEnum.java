package com.cowave.sys.job.schedule;

import lombok.Getter;

/**
 * @author xuxueli/shanhuiming
 */
@Getter
public enum TriggerTypeEnum {
    MANUAL("MANUAL"),
    SCHEDULE("SCHEDULE"),
    RETRY("RETRY"),
    PARENT("PARENT"),
    API("API"),
    MISFIRE("MISFIRE");

    TriggerTypeEnum(String title){
        this.title = title;
    }

    private final String title;
}
