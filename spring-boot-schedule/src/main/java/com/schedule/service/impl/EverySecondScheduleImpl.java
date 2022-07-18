package com.schedule.service.impl;

import com.schedule.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author yaoyinong
 * @date 2022/7/19 00:07
 * @description
 */
@Slf4j
@Component
public class EverySecondScheduleImpl implements ScheduleService {

    @Override
    public void everySecond() {
        log.info(Thread.currentThread().getName() + ":每秒执行");
    }

}
