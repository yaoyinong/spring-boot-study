package com.schedule.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yaoyinong
 * @date 2022/7/18 23:47
 * @description 定时任务封装
 * 非必需实现接口
 */
public interface ScheduleService {

    /**
     * 每秒
     */
     default void everySecond(){}

    /**
     * 每分钟
     */
    default void everyMinute(){}

    /**
     * 每五分钟
     */
    default void everyFiveMinute(){}

    /**
     * 每小时
     */
    default void everyHour(){}

    /**
     * 每天8点钟
     */
    default void everyDayEightClock(){}

}
