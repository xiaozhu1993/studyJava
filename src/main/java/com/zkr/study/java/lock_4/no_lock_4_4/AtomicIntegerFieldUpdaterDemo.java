package com.zkr.study.java.lock_4.no_lock_4_4;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author zhukerui
 * @date 2019-05-22 17:29
 * @className AtomicIntegerFieldUpdaterDemo
 * @description AtomicIntegerFieldUpdater可以对int,long和普通对象进行CAS修改,使其线程安全
 */
public class AtomicIntegerFieldUpdaterDemo {
    private static class Candidate {
        int id;
        volatile int score;
    }

    private final static AtomicIntegerFieldUpdater<Candidate> SCORE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(Candidate.class, "score");
    private static AtomicInteger allScore = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        final Candidate candidate = new Candidate();
        Thread[] threads = new Thread[10000];
        int count = 10000;
        for (int i = 0; i < count; i++) {
            threads[i] = new Thread(){
                @Override
                public void run() {
                    if (Math.random() > 0.4) {
                        SCORE_UPDATER.incrementAndGet(candidate);
                        allScore.incrementAndGet();
                    }
                }
            };
            threads[i].start();
        }
        for (int i = 0; i < count; i++) {
            threads[i].join();
        }
        System.out.println("score = " + candidate.score);
        System.out.println("allScore = " + allScore);
    }
}
