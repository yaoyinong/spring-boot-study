package com.schedule.service.impl;

import com.schedule.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author yaoyinong
 * @date 2022/7/19 00:14
 * @description
 */
@Slf4j
@Component
public class EveryMinuteScheduleImpl implements ScheduleService {

    @Override
    public void everyMinute() {
        log.info(Thread.currentThread().getName() + ":每分钟执行");
    }

}
