package com.zkr.study.java.jdk_concurrent_3.synchronous_control_3_1;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhukerui
 * @className TryLock
 * @description 重入锁-无参数等待tryLock() 立即返回结果true or false
 * @date 2019-05-20 16:31
 */
public class TryLock implements Runnable{
    private static ReentrantLock lock1 = new ReentrantLock();
    private static ReentrantLock lock2 = new ReentrantLock();
    private int i;

    private TryLock(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        ReentrantLock tempLock;
        ReentrantLock tempLock2;
        if (i == 1) {
            tempLock = lock1;
            tempLock2 = lock2;
        } else {
            tempLock = lock2;
            tempLock2 = lock1;
        }
        while (true) {
            if (tempLock.tryLock()) {
                try {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (tempLock2.tryLock()) {
                        try {
                            System.out.println(Thread.currentThread().getId() + "-" + Thread.currentThread().getName() + " my job done");
                            return;
                        } finally {
                            tempLock2.unlock();
                        }
                    }
                } finally {
                    tempLock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) {
        TryLock tryLock1 = new TryLock(1);
        TryLock tryLock2 = new TryLock(2);
        Thread thread1 = new Thread(tryLock1, "thread1");
        Thread thread2 = new Thread(tryLock2, "thread2");
        thread1.start();thread2.start();
    }
}
