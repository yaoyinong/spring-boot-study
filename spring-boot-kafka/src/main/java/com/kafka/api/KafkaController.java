package com.kafka.api;

import com.kafka.service.ProducerService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yaoyinong
 * @date 2022/7/16 00:58
 * @description
 */
@RestController
@RequestMapping("/kafka")
public class KafkaController {

    @Resource
    private ProducerService producerService;

    @PostMapping("/send/{topic}")
    public String send(@PathVariable("topic") String topic, @RequestBody String body) {
        producerService.sendMessage(topic, body);
        return "send success";
    }

}
