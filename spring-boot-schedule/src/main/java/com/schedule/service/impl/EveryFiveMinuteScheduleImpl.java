package com.schedule.service.impl;

import com.schedule.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author yaoyinong
 * @date 2022/7/19 09:29
 * @description
 */
@Slf4j
@Component
public class EveryFiveMinuteScheduleImpl implements ScheduleService {

    @Override
    public void everyFiveMinute() {
        log.info(Thread.currentThread().getName() + ":每五分钟执行");
    }

}
