package com.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @author yaoyinong
 * @date 2022/7/18 21:32
 * @description 使用自定义参数实现线程池
 * <p>
 * 线程池的五种状态
 * 1.RUNNING 线程池能够接收新任务，以及对新添加的任务进行处理
 * 2.SHUTDOWN 线程出不再接收新任务，但是还可以对已经添加的任务进行处理
 * 3.STOP 线程池不再接收新任务，不处理已添加的任务，并且会中断总在处理的任务
 * 4.TIDYING 当所有的任务已终止，队列中的任务也为0，线程池会变为TIDYING状态。
 * 当线程池变为TIDYING状态时，会执行构造函数terminated()，这个方法默认是空的，
 * 若用户想在线程池变为TIDYING时进行相应的处理，可以通过重载terminated()函数来实现
 * 5.TERMINATED 线程池彻底终止的状态
 */
@Slf4j
public class ExecutorTest2 {

    /**
     * 线程池的核心参数
     * corePollSize：核心线程数 一直在保持运行的线程
     * maximumPoolSize：最大线程数，线程池允许创建的最大线程数，队列满了之后触发创建。必须>=核心线程数
     * keepAliveTime：超出corePoolSize后（最大线程）创建的线程的存过时间
     * unit：keepAliveTime的时间单位
     * workQueue：任务队列，用于保存待执行的任务
     * threadFactory：线程池内部创建线程所用的工厂
     * handler：任务无法执行时的处理器（拒绝策略）
     * <p>
     * 线程池会调用rejectedExecutionHandler来处理拒绝任务
     * ThreadPoolExecutor类有几个内部实现类来处理拒绝任务：
     * 1.AbortPolicy 中断任务，抛出运行时异常
     * 2.CallerRunsPolicy 执行任务
     * 3.DiscardPolicy 什么都不干，相当于丢弃任务
     * 4.DiscardOldestPolicy 从队列中踢出最后一个执行的任务
     * 5.实现RejectedExecutionHandler接口，可自定义拒绝处理器
     **/
    public static ExecutorService newFixedThreadPool() {
        return new ThreadPoolExecutor(
                2,
                5,
                0L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(10),
                new MyThreadFactory(),
                new MyRejectedExecutionHandler()
        );
    }

    /**
     * 自定义拒绝策略
     */
    static class MyRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.error("任务被拒绝");
            //可以将r缓存起来（list、数据库、queue等）走补偿策略
        }
    }

    /**
     * 自定义线程工厂
     * 自定义线程名称
     */
    static class MyThreadFactory implements ThreadFactory {
        //线程池编号
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        //线程组
        private final ThreadGroup group;
        //线程编号
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        //线程名称前缀
        private final String namePrefix;

        MyThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "myPool-" + poolNumber.getAndIncrement() + "-thread";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix +"("+ threadNumber.getAndIncrement()+")", 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = ExecutorTest2.newFixedThreadPool();
        IntStream.rangeClosed(1, 30).forEach(i -> executorService.execute(() -> log.info(Thread.currentThread().getName() + "：" + i)));
    }

}
