package com.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

/**
 * @author yaoyinong
 * @date 2022/7/12 15:21
 */
@Service
@Slf4j
public class ConsumerService {

    @KafkaListener(topics = "#{kafkaTopicName}", groupId = "#{topicGroupId}")
    void processMessage(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment ack) {
        log.info("kafka消费者 收到消息, topic = {}, msg={}", consumerRecord.topic(), consumerRecord.value());
        //手动确认消息
        ack.acknowledge();
        log.info("kafka消费者 topic = {}, msg={} 消费成功", consumerRecord.topic(), consumerRecord.value());
    }

}
