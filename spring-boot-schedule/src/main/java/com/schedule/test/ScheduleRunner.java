package com.schedule.test;

import com.schedule.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yaoyinong
 * @date 2022/7/18 22:26
 * @description 定时任务测试
 */
@Component
@Slf4j
public class ScheduleRunner {

    private final List<ScheduleService> scheduleServices;

    public ScheduleRunner(List<ScheduleService> scheduleServices) {
        this.scheduleServices = scheduleServices;
    }

    @Scheduled(cron = "*/1 * * * * ?")
    @Async("customExecutor")
    public void everySecond() {
        scheduleServices.forEach(ScheduleService::everySecond);
    }

    @Scheduled(cron = "0 */1 * * * ?")
    @Async("customExecutor")
    public void everyMinute() {
        scheduleServices.forEach(ScheduleService::everyMinute);
    }

    @Scheduled(cron = "0 */5 * * * ?")
    @Async("customExecutor")
    public void everyFiveMinute() {
        scheduleServices.forEach(ScheduleService::everyFiveMinute);
    }

    @Scheduled(cron = "0 0 */1 * * ?")
    @Async("customExecutor")
    public void everyHour() {
        scheduleServices.forEach(ScheduleService::everyHour);
    }

    @Scheduled(cron = "0 0 8 * * ?")
    @Async("customExecutor")
    public void everyDayEightClock() {
        scheduleServices.forEach(ScheduleService::everyDayEightClock);
    }

}
