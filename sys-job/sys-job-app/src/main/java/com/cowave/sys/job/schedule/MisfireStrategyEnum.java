package com.cowave.sys.job.schedule;

import lombok.Getter;

/**
 * @author xuxueli/shanhuiming
 */
@Getter
public enum MisfireStrategyEnum {

    DO_NOTHING("misfire_strategy_do_nothing"),

    FIRE_ONCE_NOW("misfire_strategy_fire_once_now");

    private final String title;

    MisfireStrategyEnum(String title) {
        this.title = title;
    }

    public static MisfireStrategyEnum match(String name, MisfireStrategyEnum defaultItem){
        for (MisfireStrategyEnum item: MisfireStrategyEnum.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultItem;
    }
}
