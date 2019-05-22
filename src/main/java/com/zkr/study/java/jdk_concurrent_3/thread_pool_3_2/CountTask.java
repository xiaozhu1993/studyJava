package com.zkr.study.java.jdk_concurrent_3.thread_pool_3_2;

import com.google.common.util.concurrent.MoreExecutors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhukerui
 * @date 2019-05-22 10:48
 * @className CountTask
 * @description Fork/Join框架的简单使用 ForkJoinPool, ForkJoinTask, forkJoinPool.submit(forkJoinTask), RecursiveAction, RecursiveTask<T>
 */
public class CountTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 10000;
    private long start;
    private long end;
    private static volatile AtomicInteger computeNumber = new AtomicInteger(0);

    private CountTask(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long sum = 0;
        boolean canCompute = (end - start) < THRESHOLD;
        if (canCompute) {
            System.out.println("直接计算:" + computeNumber.addAndGet(1));
            for (long i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            System.out.println("分而治之:");
            int count = 100;
            long step = (start + end)/count;
            ArrayList<CountTask> subTasks = new ArrayList<>();
            long pos = start;
            for (int i = 0; i < count; i++) {
                long lastOne = pos + step;
                if (lastOne > end) {
                    lastOne = end;
                }
                CountTask subTask = new CountTask(pos, lastOne);
                pos += step + 1;
                subTasks.add(subTask);
                //提交子任务到forkJoin线程池
                subTask.fork();
            }
            for (CountTask countTask : subTasks) {
                sum += countTask.join();
            }
        }
        return sum;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        CountTask countTask = new CountTask(0, 200000L);
        ForkJoinTask<Long> result = forkJoinPool.submit(countTask);
        System.out.println("sum = " + result.get());

        //Guava 特殊的DirectExecutor线程池
        /*Executor executor = MoreExecutors.directExecutor();
        executor.execute( () -> System.out.println("I am running in " + Thread.currentThread().getName()));*/

        //Guava Daemon线程池
        ThreadPoolExecutor executor1 = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        MoreExecutors.getExitingExecutorService(executor1);
        executor1.execute( () -> System.out.println("I am running in " + Thread.currentThread().getName()));

    }
}
