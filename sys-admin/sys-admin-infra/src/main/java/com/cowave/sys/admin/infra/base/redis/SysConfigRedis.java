package com.cowave.sys.admin.infra.base.redis;

import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.commons.framework.helper.redis.dict.DictValueParser;
import com.cowave.sys.admin.domain.base.SysConfig;
import com.cowave.sys.admin.infra.base.dao.SysConfigDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shanhuiming
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SysConfigRedis implements ApplicationRunner {
    private static final String KEY_CONFIG = "admin:config:";
    private final Map<String, DictValueParser> parserMap = new ConcurrentHashMap<>();
    private final RedisHelper redisHelper;
    private final SysConfigDao sysConfigDao;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("init cache of config ...");
        refreshConfig();
    }

    public void refreshConfig() throws Exception {
        Collection<String> keys = redisHelper.keys(KEY_CONFIG + "*");
        redisHelper.delete(keys);
        List<SysConfig> list = sysConfigDao.list();
        for (SysConfig conf : list) {
            putConfig(conf);
        }
    }

    public void putConfig(SysConfig conf) throws Exception {
        String parserClazz = conf.getValueParser();
        if (StringUtils.isBlank(parserClazz)) {
            redisHelper.putValue(KEY_CONFIG + conf.getConfigKey(), conf.getConfigValue());
        } else {
            DictValueParser parser = parserMap.get(parserClazz);
            if (parser == null) {
                parser = (DictValueParser) Class.forName(parserClazz).getDeclaredConstructor().newInstance();
                parserMap.put(parserClazz, parser);
            }
            redisHelper.putValue(KEY_CONFIG + conf.getConfigKey(), parser.parse(conf.getConfigValue(), conf.getValueParam()));
        }
    }

    public void removeConfig(SysConfig conf) {
        redisHelper.delete(KEY_CONFIG + conf.getConfigKey());
    }

    public <T> T getConfigValue(String configKey) {
        return redisHelper.getValue(KEY_CONFIG + configKey);
    }
}
