package com.cowave.sys.job.route;

import com.cowave.sys.job.route.impl.*;
import lombok.Getter;

/**
 * @author xuxueli/shanhuiming
 */
@Getter
public enum ExecutorRouteStrategyEnum {
    FIRST("jobconf_route_first", new ExecutorRouteFirst()),
    LAST("jobconf_route_last", new ExecutorRouteLast()),
    ROUND("jobconf_route_round", new ExecutorRouteRound()),
    RANDOM("jobconf_route_random", new ExecutorRouteRandom()),
    CONSISTENT_HASH("jobconf_route_consistenthash", new ExecutorRouteConsistentHash()),
    LEAST_FREQUENTLY_USED("jobconf_route_lfu", new ExecutorRouteLFU()),
    LEAST_RECENTLY_USED("jobconf_route_lru", new ExecutorRouteLRU()),
    FAILOVER("jobconf_route_failover", new ExecutorRouteFailover()),
    BUSYOVER("jobconf_route_busyover", new ExecutorRouteBusyOver()),
    SHARDING_BROADCAST("jobconf_route_shard", null);

    ExecutorRouteStrategyEnum(String title, ExecutorRouter router) {
        this.title = title;
        this.router = router;
    }

    private final String title;

    private final ExecutorRouter router;

    public static ExecutorRouteStrategyEnum match(String name, ExecutorRouteStrategyEnum defaultItem){
        if (name != null) {
            for (ExecutorRouteStrategyEnum item: ExecutorRouteStrategyEnum.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
        }
        return defaultItem;
    }
}
