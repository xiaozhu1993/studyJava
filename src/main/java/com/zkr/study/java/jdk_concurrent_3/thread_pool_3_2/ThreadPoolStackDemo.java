package com.zkr.study.java.jdk_concurrent_3.thread_pool_3_2;

import java.util.concurrent.*;

/**
 * @author zhukerui
 * @date 2019-05-22 10:00
 * @className ThreadPoolStackDemo
 * @description 线程池中的堆栈信息
 */
public class ThreadPoolStackDemo {
    private static class DivTask implements Runnable {
        int a, b;

        public DivTask(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public void run() {
            double result = a/b;
            System.out.println(result);
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 0L, TimeUnit.SECONDS,
                new SynchronousQueue<>());
        int count = 5;
        for (int i = 0; i < count; i++) {
            //方法一: submit() 不打印任何异常堆栈信息
            /*threadPoolExecutor.submit(new DivTask(100, i));*/
            //方法二: Future submit() 可以打印异常堆栈信息, 且程序无法正常继续执行
            Future future = threadPoolExecutor.submit(new DivTask(100, i));
            future.get();
            //方法三: execute() 可以打印异常堆栈信息,但是仅有异常发生的地方
            /*threadPoolExecutor.execute(new DivTask(100, i));*/
        }
    }
}
