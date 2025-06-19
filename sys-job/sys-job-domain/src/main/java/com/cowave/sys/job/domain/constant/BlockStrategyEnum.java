package com.cowave.sys.job.domain.constant;

import lombok.Getter;

/**
 * @author xuxueli/shanhuiming
 */
@Getter
public enum BlockStrategyEnum {

    /**
     * SERIAL_EXECUTION
     */
    SERIAL_EXECUTION("Serial execution"),

    /*CONCURRENT_EXECUTION("并行"),*/

    /**
     * DISCARD_LATER
     */
    DISCARD_LATER("Discard Later"),

    /**
     * COVER_EARLY
     */
    COVER_EARLY("Cover Early");

    private final String title;

    BlockStrategyEnum(String title) {
        this.title = title;
    }

    public static BlockStrategyEnum match(String name, BlockStrategyEnum defaultItem) {
        if (name != null) {
            for (BlockStrategyEnum item: BlockStrategyEnum.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
        }
        return defaultItem;
    }
}
