package com.cowave.sys.admin.infra.base.kafka;

import com.alibaba.fastjson.JSON;
import com.cowave.sys.admin.domain.base.dto.SysLogDto;
import com.cowave.sys.admin.infra.base.dao.mapper.dto.SysLogDtoMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class SysLogKafka {

    private final SysLogDtoMapper sysLogMapper;

    @KafkaListener(topics = {"${spring.access.oplog-kafka:sys-oplog}"})
    public void consume(ConsumerRecord<?, ?> message) {
        SysLogDto sysLog = JSON.parseObject(String.valueOf(message.value()), SysLogDto.class);
        sysLogMapper.insert(sysLog);
    }
}
