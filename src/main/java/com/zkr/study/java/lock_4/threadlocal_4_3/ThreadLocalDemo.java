package com.zkr.study.java.lock_4.threadlocal_4_3;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @author zhukerui
 * @date 2019-05-24 15:22
 * @className ThreadLocalDemo
 * @description ThreadLocal性能测试
 */
public class ThreadLocalDemo {
    private static final int COUNT = 10000000;
    private static final int THREAD_COUNT = 4;
    private static ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
    private static Random random = new Random(123);
    private static ThreadLocal<Random> threadLocalRandom = ThreadLocal.withInitial(() -> new Random(123));

    private static class RandTask implements Callable<Long> {
        private int mode;

        RandTask(int mode) {
            this.mode = mode;
        }

        Random getRandom() {
            if (mode == 0) {
                return random;
            } else if (mode == 1) {
                return threadLocalRandom.get();
            } else {
                return null;
            }
        }
        @Override
        public Long call() {
            long start = System.currentTimeMillis();
            for (int i = 0; i < COUNT; i++) {
                getRandom().nextInt();
            }
            long end = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + " spend " + (end - start) + "ms");
            return end - start;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        @SuppressWarnings("unchecked")
        Future<Long>[] futures = new Future[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            futures[i] = executorService.submit(new RandTask(0));
        }
        long totalTime = 0;
        for (int i = 0; i < THREAD_COUNT; i++) {
            totalTime += futures[i].get();
        }
        System.out.println("多线程访问同一个Random实例:" + totalTime + "ms");

        //ThreadLocal
        for (int i = 0; i < THREAD_COUNT; i++) {
            futures[i] = executorService.submit(new RandTask(1));
        }
        totalTime = 0;
        for (int i = 0; i < THREAD_COUNT; i++) {
            totalTime += futures[i].get();
        }
        System.out.println("使用ThreadLocal包装Random实例:" + totalTime + "ms");
        executorService.shutdown();
    }
}
