package com.zkr.study.java.jdk_concurrent_3.synchronous_control_3_1;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @author zhukerui
 * @date 2019-05-21 14:24
 * @className RateLimiterDemo
 * @description Google核心库Guava中的限流工具RateLimiter,采用令牌桶算法
 */
public class RateLimiterDemo {
    private static double permitsPerSecond = 2;
    static RateLimiter rateLimiter = RateLimiter.create(permitsPerSecond);

    private static class Task implements Runnable {

        @Override
        public void run() {
            System.out.println(System.currentTimeMillis());
        }
    }

    public static void main(String[] args) {
        int count = 50;
        for (int i = 0; i < count; i++) {
            //拿到令牌,继续执行;否则,阻塞
            rateLimiter.acquire();
            //拿到令牌,返回true;否则,返回false
            /*if (!rateLimiter.tryAcquire()) {
                continue;
            }*/
            new Thread(new Task()).start();
        }
    }
}
