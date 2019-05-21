package com.zkr.study.java.jdk_concurrent_3.synchronous_control_3_1;

import java.util.concurrent.locks.LockSupport;

/**
 * @author zhukerui
 * @date 2019-05-21 13:07
 * @className LockSupportDemo
 * @description 线程阻塞工具类-1.线程内任意位置阻塞线程 2.优于Thread.suspend()和Object.wait()
 */
public class LockSupportDemo {
    private static final Object OBJECT = new Object();
    private static ChangeObjectThread thread1 = new ChangeObjectThread("thread1");
    private static ChangeObjectThread thread2 = new ChangeObjectThread("thread2");

    public static class ChangeObjectThread extends Thread {
        ChangeObjectThread(String name) {
            super.setName(name);
        }

        @Override
        public void run() {
            synchronized (OBJECT) {
                System.out.println("in " + getName());
                LockSupport.park();
                if (Thread.interrupted()) {
                    System.out.println(getName() + " 被中断了");
                }
            }
            System.out.println(getName() + " 执行结束");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        thread1.start();
        Thread.sleep(100);
        thread2.start();
        //模拟中断响应功能 start
        thread1.interrupt();
        LockSupport.unpark(thread2);
        //模拟中断响应功能 end
        //验证LockSupport的类信号量机制
        //即park()方法是通过判断许可是否可用来决定是否返回还是阻塞
        //unpark()方法则是将许可变为可用状态
        //因此,unpark()方法和park()方法谁先执行并不影响程序后续执行
        /*LockSupport.unpark(thread1);
        LockSupport.unpark(thread2);
        thread1.join();
        thread2.join();*/
    }
}
