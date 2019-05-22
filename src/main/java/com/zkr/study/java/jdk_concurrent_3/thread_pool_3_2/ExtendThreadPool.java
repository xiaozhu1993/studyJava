package com.zkr.study.java.jdk_concurrent_3.thread_pool_3_2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhukerui
 * @date 2019-05-22 9:17
 * @className ExtendThreadPool
 * @description 自定义拓展线程池
 */
public class ExtendThreadPool {
    private static class Task implements Runnable {
        private String name;

        Task(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println("正在执行 Thread ID:" + Thread.currentThread().getId() + ", Task Name: " + name);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>()){
            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                System.out.println("准备执行: " + ((Task) r).name);
            }

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                System.out.println("执行完成: " + ((Task) r).name);
            }

            @Override
            protected void terminated() {
                System.out.println("线程池退出");
            }
        };

        int count = 5;
        for (int i = 0; i < count; i++) {
            Task task = new Task("TASK-GEYM-" + i);
            executorService.execute(task);
            Thread.sleep(10);
        }
        //线程池中合理线程数量计算公式: nThreads = nCpu * uCpu * (1 + W/C)
        //nCpu: 可用CPU个数, uCpu: 目标CPU使用率, W: 等待时间, C: 计算时间
        System.out.println("可用CPU个数: " + Runtime.getRuntime().availableProcessors());
        executorService.shutdown();
    }
}
