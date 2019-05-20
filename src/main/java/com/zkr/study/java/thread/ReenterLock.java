package com.zkr.study.java.thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhukerui
 * @className ReenterLock
 * @description 重入锁
 * @date 2019-05-20 15:16
 */
public class ReenterLock implements Runnable {
    private static ReentrantLock lock = new ReentrantLock();
    private static int i = 0;

    @Override
    public void run() {
        int num = 10000000;
        for (int j = 0; j < num; j++) {
            lock.lock();
            lock.lock();
            try {
                i++;
            } finally {
                lock.unlock();
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReenterLock reenterLock = new ReenterLock();
        Thread thread1 = new Thread(reenterLock);
        Thread thread2 = new Thread(reenterLock);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(i);
    }
}
