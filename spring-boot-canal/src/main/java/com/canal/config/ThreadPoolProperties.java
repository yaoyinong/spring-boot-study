package com.canal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yaoyinong
 * @date 2022/7/18 23:29
 * @description 线程池相关参数
 */
@Data
@Component
@ConfigurationProperties("thread-pool")
public class ThreadPoolProperties {

    /**
     * 设置核心线程数
     */
    private int corePoolSize;

    /**
     * 设置最大线程数
     */
    private int maxPoolSize;

    /**
     * 设置缓冲队列大小
     */
    private int queueCapacity;

    /**
     * 设置线程的最大空闲时间，超过了核心线程数之外的线程，在空闲时间到达之后会被销毁
     */
    private int keepAliveSeconds;

    /**
     * 设置线程名字的前缀，设置好了之后可以方便我们定位处理任务所在的线程池
     */
    private String threadNamePrefix;

}
