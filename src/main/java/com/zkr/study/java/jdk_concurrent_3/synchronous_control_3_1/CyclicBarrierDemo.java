package com.zkr.study.java.jdk_concurrent_3.synchronous_control_3_1;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author zhukerui
 * @date 2019-05-21 9:59
 * @className CyclicBarrierDemo
 * @description 循环栅栏-循环计数,使线程多次等待,以达成多种目标
 */
public class CyclicBarrierDemo {
    private static class Soldier implements Runnable {
        private String soldierName;
        private final CyclicBarrier cyclicBarrier;

        Soldier(String soldierName, CyclicBarrier cyclicBarrier) {
            this.soldierName = soldierName;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                //第一次计数:等待所有士兵集结完成
                cyclicBarrier.await();
                doWork();
                //第二次计数:等待所有士兵任务完成
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        void doWork() throws InterruptedException {
            Thread.sleep(Math.abs(new Random().nextInt()%10000));
            System.out.println(soldierName + ":任务完成");
        }
    }

    private static class BarrierRun implements Runnable {
        boolean flag;
        int n;

        BarrierRun(boolean flag, int n) {
            this.flag = flag;
            this.n = n;
        }

        @Override
        public void run() {
            if (flag) {
                System.out.println("司令:[士兵 " + n + " 个, 任务完成! ]");
            } else {
                System.out.println("司令:[士兵 " + n + " 个, 集合完毕! ]");
                flag = true;
            }
        }
    }

    public static void main(String[] args) {
        final int count = 10;
        Thread[] allSoldier = new Thread[count];
        //设置计数器,每次计数要求为10,计数完成后执行BarrierRun
        CyclicBarrier cyclicBarrier = new CyclicBarrier(count, new BarrierRun(false, count));
        System.out.println("集合队伍!");
        for (int i = 0; i < count; i++) {
            System.out.println("士兵" + i + ", 前来报道!");
            allSoldier[i] = new Thread(new Soldier("士兵" + i, cyclicBarrier));
            allSoldier[i].start();
            //模拟线程中断,导致循环栅栏损坏并抛出异常
            /*if (i == 5) {
                allSoldier[0].interrupt();
            }*/
        }
    }
}
