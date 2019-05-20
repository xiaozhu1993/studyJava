package com.zkr.study.java.thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhukerui
 * @className FairLock
 * @description 重入锁-公平锁
 * @date 2019-05-20 17:00
 */
public class FairLock implements Runnable {
    private static ReentrantLock lock = new ReentrantLock(true);

    @Override
    public void run() {
        while (true) {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " 获得锁");
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        FairLock fairLock = new FairLock();
        Thread thread1 = new Thread(fairLock, "Thread-t1");
        Thread thread2 = new Thread(fairLock, "Thread-t2");
        thread1.start(); thread2.start();
    }
}
