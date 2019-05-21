package com.zkr.study.java.jdk_concurrent_3.thread_pool_3_2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhukerui
 * @date 2019-05-21 15:51
 * @className ThreadPoolDemo
 * @description Executor-newFixedThreadPool 固定大小的线程池
 */
public class ThreadPoolDemo {
    private static class Task implements Runnable {

        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + ":Thread ID:" + Thread.currentThread().getId());
            int millis = 1000;
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Task task = new Task();
        int nThreads = 5;
        int count = nThreads * 2;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        /*ExecutorService executorService = Executors.newCachedThreadPool();*/
        for (int i = 0; i < count; i++) {
            executorService.submit(task);
        }
    }
}
