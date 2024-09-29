package com.cowave.sys.admin.infra.base.kafka;

import com.alibaba.fastjson.JSON;
import com.cowave.sys.admin.domain.base.dto.SysAlarmDto;
import com.cowave.sys.admin.infra.base.dao.mapper.dto.SysAlarmDtoMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class SysAlarmKafka {

    private final SysAlarmDtoMapper sysAlarmMapper;

    @KafkaListener(topics = {"${spring.access.alarm.kafka-topic:sys-alarm}"})
    public void recordAlarm(ConsumerRecord<?, ?> message) {
        SysAlarmDto sysAlarm = JSON.parseObject(String.valueOf(message.value()), SysAlarmDto.class);
        if(sysAlarmMapper.alarmIncrease(sysAlarm) == 0) {
            sysAlarmMapper.insert(sysAlarm);
        }
    }
}
