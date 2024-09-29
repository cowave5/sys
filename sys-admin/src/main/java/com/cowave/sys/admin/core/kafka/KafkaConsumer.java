/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.core.kafka;

import com.alibaba.fastjson.JSON;
import com.cowave.sys.admin.api.service.SysAlarmService;
import com.cowave.sys.admin.api.service.SysLogService;
import com.cowave.sys.model.admin.SysAlarm;
import com.cowave.sys.model.admin.SysLog;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class KafkaConsumer {

    private final SysAlarmService sysAlarmService;

    private final SysLogService sysLogService;

    @KafkaListener(topics = {"${spring.access.oplog-kafka:access-oplog}"})
    public void recordOperation(ConsumerRecord<?, ?> message) {
        SysLog sysLog = JSON.parseObject(String.valueOf(message.value()), SysLog.class);
        sysLogService.add(sysLog);
    }

    @KafkaListener(topics = {"${spring.access.alarm.kafka-topic:access-alarm}"})
    public void recordAlarm(ConsumerRecord<?, ?> message) {
        SysAlarm sysAlarm = JSON.parseObject(String.valueOf(message.value()), SysAlarm.class);
        sysAlarmService.add(sysAlarm);
    }
}
