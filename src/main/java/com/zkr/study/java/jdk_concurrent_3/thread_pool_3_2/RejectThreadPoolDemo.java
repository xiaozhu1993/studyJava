package com.zkr.study.java.jdk_concurrent_3.thread_pool_3_2;

import java.util.concurrent.*;

/**
 * @author zhukerui
 * @date 2019-05-22 7:33
 * @className RejectThreadPoolDemo
 * @description 1.自定义线程池和拒绝策略 2.自定义线程创建
 */
public class RejectThreadPoolDemo {
    private static class Task implements Runnable {

        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + " Thread ID:" + Thread.currentThread().getId());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 自定义线程池和拒绝策略
     */
    /*public static void main(String[] args) throws InterruptedException {
        Task task = new Task();
        ExecutorService executorService = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(10), Executors.defaultThreadFactory(),
                (r, executor) -> System.out.println(r.toString() + " is discard"));
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            executorService.submit(task);
            Thread.sleep(10);
        }
    }*/

    /**
     * 自定义线程创建
     */
    public static void main(String[] args) throws InterruptedException {
        Task task = new Task();
        ExecutorService executorService = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(), (r -> {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    System.out.println("create " + thread);
                    return thread;
                }));
        int count = 5;
        for (int i = 0; i < count; i++) {
            executorService.submit(task);
        }
        Thread.sleep(2000);
    }
}
