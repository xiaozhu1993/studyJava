package com.zkr.study.java.thread;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author zhukerui
 * @date 2019-05-20 22:58
 * @className ReadWriteLockDemo
 * @description 重入锁-读写锁 读写分离，读频次相较于写频次越高，性能提升越明显
 */
public class ReadWriteLockDemo {
    private static Lock lock = new ReentrantLock();
    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static Lock readLock = readWriteLock.readLock();
    private static Lock writeLock = readWriteLock.writeLock();
    private int value;
    private int millis = 1000;

    private Object handleRead(Lock lock) throws InterruptedException {
        try {
            lock.lock();
            Thread.sleep(millis);
            return value;
        } finally {
            lock.unlock();
        }
    }

    private void handleWrite(Lock lock, int index) throws InterruptedException {
        try {
            lock.lock();
            Thread.sleep(millis);
            value = index;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        final ReadWriteLockDemo readWriteLockDemo = new ReadWriteLockDemo();
        Runnable readRunnable = () -> {
            try {
                Object result = readWriteLockDemo.handleRead(readLock);
//                Object result = readWriteLockDemo.handleRead(lock);
                System.out.println(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable writeRunnable = () -> {
            try {
                int bound = 1000;
                int i = new Random().nextInt(bound);
                System.out.println("write: " + i);
                readWriteLockDemo.handleWrite(writeLock, i);
//                readWriteLockDemo.handleWrite(lock, i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        int readNumbers = 18;
        int writeNumbers = 20;

        for (int i = readNumbers; i < writeNumbers; i++) {
            new Thread(writeRunnable).start();
        }

        for (int i = 0; i < readNumbers; i++) {
            new Thread(readRunnable).start();
        }
    }
}
