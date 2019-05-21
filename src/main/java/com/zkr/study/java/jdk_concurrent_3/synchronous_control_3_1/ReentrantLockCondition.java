package com.zkr.study.java.jdk_concurrent_3.synchronous_control_3_1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhukerui
 * @className ReentrantLockCondition
 * @description 重入锁-Condition
 * @date 2019-05-20 17:33
 */
public class ReentrantLockCondition implements Runnable {
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    @Override
    public void run() {
        lock.lock();
        try {
            condition.await();
            System.out.println("Thread is going on");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
          lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLockCondition reentrantLockCondition = new ReentrantLockCondition();
        Thread thread = new Thread(reentrantLockCondition);
        thread.start();
        Thread.sleep(5000);
        //通知线程thread1继续执行
        lock.lock();
        condition.signal();
        lock.unlock();
    }
}
