package com.zkr.study.java.mode_5;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhukerui
 * @date 2019-05-26 13:34
 * @className ProducerConsumerDemo
 * @description 生产者消费者Demo
 */
public class ProducerConsumerDemo {
    private static final int SLEEP_TIME = 1000;

    /**
     * 采用不变模式，构建内部类，作为生产者和消费者的共享数据模型
     */
    private static final class PcData {
        private final int intData;

        private PcData(int d) {
            intData = d;
        }

        int getIntData() {
            return intData;
        }

        @Override
        public String toString() {
            return "data:" + intData;
        }
    }

    /**
     * 生产者
     */
    private static class Producer implements Runnable {
        private BlockingQueue<PcData> blockingQueue;
        private volatile boolean isRunning = true;
        private AtomicInteger count = new AtomicInteger();

        Producer(BlockingQueue<PcData> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        private void stop() {
            isRunning = false;
        }

        @Override
        public void run() {
            PcData data;
            Random random = new Random();
            System.out.println("start producer " + Thread.currentThread().getId());
            try {
                while (isRunning) {
                    Thread.sleep(random.nextInt(SLEEP_TIME));
                    data = new PcData(count.incrementAndGet());
                    if (!blockingQueue.offer(data, 2, TimeUnit.SECONDS)) {
                        System.err.println("failed to put data: " + data);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 消费者
     */
    private static class Consumer implements Runnable {
        private BlockingQueue<PcData> blockingQueue;

        Consumer(BlockingQueue<PcData> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        @Override
        public void run() {
            Random random = new Random();

            System.out.println("start consumer " + Thread.currentThread().getId());
            try {
                while (true) {
                    PcData data = blockingQueue.take();
                    if (null != data) {
                        int result = data.getIntData() * data.getIntData();
                        System.out.println(data.getIntData() + " * " + data.getIntData() + " = " + result);
                        Thread.sleep(random.nextInt(SLEEP_TIME));
                    } else {
                        return;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //构建共享数据模型
        BlockingQueue<PcData> blockingQueue = new LinkedBlockingDeque<>(10);
        //构建生产者
        Producer producer1 = new Producer(blockingQueue);
        Producer producer2 = new Producer(blockingQueue);
        Producer producer3 = new Producer(blockingQueue);
        //构建消费者
        Consumer consumer1 = new Consumer(blockingQueue);
        Consumer consumer2 = new Consumer(blockingQueue);
        Consumer consumer3 = new Consumer(blockingQueue);
        //构建线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        //启动线程
        executorService.execute(producer1);
        executorService.execute(producer2);
        executorService.execute(producer3);
        executorService.execute(consumer1);
        executorService.execute(consumer2);
        executorService.execute(consumer3);
        //线程睡眠，模拟实际任务执行时间
        Thread.sleep(1000);
        //停止生产者
        producer1.stop();
        producer2.stop();
        producer3.stop();
        //线程睡眠，模拟实际任务执行时间
        Thread.sleep(3000);
        //关闭线程池
        executorService.shutdown();
    }
}
