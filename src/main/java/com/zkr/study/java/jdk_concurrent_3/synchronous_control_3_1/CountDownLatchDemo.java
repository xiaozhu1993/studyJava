package com.zkr.study.java.jdk_concurrent_3.synchronous_control_3_1;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhukerui
 * @className CountDownLatchDemo
 * @description 倒数计数器-控制线程等待,达到某一时刻继续执行
 * @date 2019-05-21 9:24
 */
public class CountDownLatchDemo implements Runnable {
    private static int count = 10;
    private final static CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(count);
    private final static CountDownLatchDemo COUNT_DOWN_LATCH_DEMO = new CountDownLatchDemo();

    @Override
    public void run() {
        try {
            //模拟检查线程
            Thread.sleep(new Random().nextInt(count)*1000);
            System.out.println("check complete");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            COUNT_DOWN_LATCH.countDown();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        for (int i = 0; i < count; i++) {
            executorService.submit(COUNT_DOWN_LATCH_DEMO);
        }
        //线程等待检查
        COUNT_DOWN_LATCH.await();
        //火箭发射
        System.out.println("launch!");
        executorService.shutdown();
    }
}
