package com.schedule.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yaoyinong
 * @date 2022/7/18 22:28
 * @description 实现Spring线程池
 */
@Configuration
@EnableAsync
@Slf4j
public class ExecutorConfig implements AsyncConfigurer {

    @Autowired
    private ThreadPoolProperties threadPoolProperties;

    @Bean(name = "customExecutor")
    public Executor asyncServiceExecutor() {
        log.info("开启SpringBoot的线程池");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        executor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());
        executor.setThreadNamePrefix(threadPoolProperties.getThreadNamePrefix());
        //设置拒绝策略：当线程池达到最大线程数时，如何处理新任务
        //CALLER_RUNS：在添加到线程池失败时会由主线程自己来执行这个任务，如果执行程序已关闭，则会丢弃该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        log.info("初始化线程池-配置信息-核心线程数:{} 最大线程数:{} 缓冲队列大小:{} 线程最大空闲时间:{} 线程名字前缀:{}",
                executor.getCorePoolSize(), executor.getMaxPoolSize(), executor.getQueueCapacity(), executor.getKeepAliveSeconds(), executor.getThreadNamePrefix());
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return asyncServiceExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> log.info(String.format("执行异步任务%s", method), ex);
    }

}
