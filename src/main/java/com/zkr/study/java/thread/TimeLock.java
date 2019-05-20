package com.zkr.study.java.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhukerui
 * @className TimeLock
 * @description 重入锁-限时等待tryLock(timeout, timeUnit)
 * @date 2019-05-20 16:17
 */
public class TimeLock implements Runnable {
    private static ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {
        int timeout = 5;
        try {
            if (lock.tryLock(timeout, TimeUnit.SECONDS)) {
                Thread.sleep(10000);
            } else {
                System.out.println("get lock failed");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        TimeLock timeLock = new TimeLock();
        Thread thread1 = new Thread(timeLock);
        Thread thread2 = new Thread(timeLock);
        thread1.start();thread2.start();
    }
}
