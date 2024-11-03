/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core;

import com.alibaba.fastjson.JSON;
import com.cowave.sys.admin.core.entity.dto.SysAlarmDto;
import com.cowave.sys.admin.core.entity.dto.SysLogDto;
import com.cowave.sys.admin.core.service.SysAlarmService;
import com.cowave.sys.admin.core.service.SysLogService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class SysKafkaHelper {

    private final SysAlarmService sysAlarmService;

    private final SysLogService sysLogService;

    @KafkaListener(topics = {"${spring.access.oplog-kafka:access-oplog}"})
    public void recordOperation(ConsumerRecord<?, ?> message) {
        SysLogDto sysLog = JSON.parseObject(String.valueOf(message.value()), SysLogDto.class);
        sysLogService.add(sysLog);
    }

    @KafkaListener(topics = {"${spring.access.alarm.kafka-topic:access-alarm}"})
    public void recordAlarm(ConsumerRecord<?, ?> message) {
        SysAlarmDto sysAlarm = JSON.parseObject(String.valueOf(message.value()), SysAlarmDto.class);
        sysAlarmService.add(sysAlarm);
    }
}
