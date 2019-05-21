package com.zkr.study.java.jdk_concurrent_3.synchronous_control_3_1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhukerui
 * @date 2019-05-20 22:04
 * @className SemaphoreDemo
 * @description 重入锁-信号量
 */
public class SemaphoreDemo implements Runnable {
    private int permits = 5;
    private int initialValue = 0;
    private final Semaphore semaphore = new Semaphore(permits);
    private AtomicInteger atomicInteger = new AtomicInteger(initialValue);
    private static volatile boolean flag = false;

    @Override
    public void run() {
        try {
            int millis = 2000;
            semaphore.acquire();
            Thread.sleep(millis);
            System.out.println(Thread.currentThread().getId() + " done!" + atomicInteger.addAndGet(1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
            int max = 20;
            if (atomicInteger.get() == max) {
                flag = true;
                System.out.println("true");
            }
        }
    }

    public static void main(String[] args) {
        int nThreads = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        final SemaphoreDemo semaphoreDemo = new SemaphoreDemo();
        for (int i = 0; i < nThreads; i++) {
            executorService.submit(semaphoreDemo);
        }
        while (true) {
            if (flag) {
                executorService.shutdown();
                return;
            }
        }
    }
}
