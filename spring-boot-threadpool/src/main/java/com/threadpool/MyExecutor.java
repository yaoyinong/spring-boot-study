package com.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author yaoyinong
 * @date 2022/7/18 21:04
 * @description 自己使用linkedBlockingQueue实现一个简单的线程池
 *
 */
@Slf4j
public class MyExecutor {

    private final LinkedBlockingQueue<Runnable> linkedBlockingQueue;

    private volatile boolean isRun = true;

    /**
     * 定义一个线程池
     * @param capacity 容器的容量
     * @param coreThreadCount 核心线程数（一直运行）
     */
    public MyExecutor(int capacity, int coreThreadCount) {
        linkedBlockingQueue = new LinkedBlockingQueue<>(capacity);
        for (int i = 0; i < coreThreadCount; i++) {
            new Thread(() -> {
                //容器中还有任务的话不能停止
                while (isRun || !linkedBlockingQueue.isEmpty()) {
                    try {
                        //一直在运行的线程会从容器中取出任务来执行，如果没有任务的话等待三秒
                        Runnable poll = linkedBlockingQueue.poll(3, TimeUnit.SECONDS);
                        if (poll != null) {
                            poll.run();
                        }
                    } catch (InterruptedException e) {
                        log.error("取出线程任务失败:{}", e.getMessage(), e);
                        Thread.currentThread().interrupt();
                    }

                }
            }, "MyPoolThread-" + i).start();
        }
    }

    /**
     * 向线程池中放入任务
     * @param runnable 线程任务
     */
    public void execute(Runnable runnable) {
        linkedBlockingQueue.offer(runnable);
    }

    /**
     * 停止线程池
     */
    public void shutdown() {
        this.isRun = false;
    }


    public static void main(String[] args) {
        MyExecutor myExecutor = new MyExecutor(20, 3);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            myExecutor.execute(() -> log.info(Thread.currentThread().getName() + "：执行任务" + finalI));
        }
        //myExecutor.shutdown();
    }

}
