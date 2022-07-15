package com.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;

/**
 * @author yaoyinong
 * @date 2022/7/12 15:21
 */
@Service
@Slf4j
public class ProducerService {

    @Resource
    private KafkaTemplate<Integer, String> kafkaTemplate;

    public void sendMessage(String topic, String data) {
        log.info("kafka生产者 开始发送消息...");
        ListenableFuture<SendResult<Integer, String>> send = kafkaTemplate.send(topic, data);
        send.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("kafka生产者 发送消息出错, ex = {}, topic = {}, data = {}", throwable, topic, data);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> integerStringSendResult) {
                log.info("kafka生产者 发送消息成功, topic = {}, data = {}", topic, data);
            }
        });

        log.info("kafka生产者 发送消息完毕");
    }
}
