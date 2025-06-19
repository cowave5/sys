package com.cowave.sys.job.schedule;

import lombok.Getter;

/**
 * @author xuxueli/shanhuiming
 */
@Getter
public enum ScheduleTypeEnum {

    NONE("schedule_type_none"),

    CRON("schedule_type_cron"),

    FIX_RATE("schedule_type_fix_rate"),

    FIX_DELAY("schedule_type_fix_delay");

    private final String title;

    ScheduleTypeEnum(String title) {
        this.title = title;
    }

    public static ScheduleTypeEnum match(String name, ScheduleTypeEnum defaultItem){
        for (ScheduleTypeEnum item: ScheduleTypeEnum.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultItem;
    }
}
