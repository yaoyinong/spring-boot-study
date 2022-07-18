package com.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author yaoyinong
 * @date 2022/7/18 21:28
 * @description jdk自带的线程池测试
 * 缺点：大部分jdk自带线程池实现类内部都使用了无界队列，有可能会导致内存溢出
 */
@Slf4j
public class ExecutorTest1 {

    public static void main(String[] args) {
        //可缓存的线程池
//        ExecutorService executorService = Executors.newCachedThreadPool();
        //自定义长度线程池
//        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //单例线程池
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
        //可定时线程池
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            executorService.schedule(() -> log.info(Thread.currentThread().getName() + "：" + finalI), 3, TimeUnit.SECONDS);
        }
    }

}
