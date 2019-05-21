package com.zkr.study.java.jdk_concurrent_3.synchronous_control_3_1;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhukerui
 * @className IntLock
 * @description 重入锁-中断响应
 * @date 2019-05-20 15:51
 */
public class IntLock implements Runnable {
    private static ReentrantLock lock1 = new ReentrantLock();
    private static ReentrantLock lock2 = new ReentrantLock();
    private int i;

    /**
     * 控制加锁顺序,方便构造死锁
     * @param i i
     */
    private IntLock(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        try {
            int param1 = 1;
            int param2 = 2;
            if (i == param1) {
                lock1.lockInterruptibly();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock2.lockInterruptibly();
            } else if (i == param2) {
                lock2.lockInterruptibly();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock1.lockInterruptibly();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock1.isHeldByCurrentThread()) {
                lock1.unlock();
            } else if (lock2.isHeldByCurrentThread()) {
                lock2.unlock();
            }
            System.out.println(Thread.currentThread().getId() + "-" + Thread.currentThread().getName() + ":线程退出");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        IntLock intLock1 = new IntLock(1);
        IntLock intLock2 = new IntLock(2);
        Thread thread1 = new Thread(intLock1, "thread1");
        Thread thread2 = new Thread(intLock2, "thread2");
        thread1.start();thread2.start();
        Thread.sleep(1000);
        thread1.interrupt();
    }
}
