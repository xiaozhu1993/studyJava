package com.zkr.study.java.jdk_concurrent_3.thread_pool_3_2;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhukerui
 * @date 2019-05-21 17:39
 * @className ScheduledExecutorServiceDemo
 * @description 测试 scheduleAtFixedRate(); period参数大于线程执行时间,则间隔时间为period, period参数小于线程执行时间,则间隔时间为线程执行时间
 */
public class ScheduledExecutorServiceDemo {
    private static int corePoolSize = 10;
    private static int initialDelay = 1;
    private static volatile AtomicInteger exitNumber = new AtomicInteger(initialDelay);
    public static void main(String[] args) {
        int period = 2;
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(corePoolSize);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                Thread.sleep(3000);
                System.out.println(System.currentTimeMillis()/1000);
                if (exitNumber.getAndAdd(1) == corePoolSize) {
                    throw new InterruptedException();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, initialDelay, period, TimeUnit.SECONDS);
    }
}
