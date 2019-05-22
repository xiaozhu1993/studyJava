package com.zkr.study.java.lock_4.no_lock_4_4;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author zhukerui
 * @date 2019-05-22 16:08
 * @className AtomicStampedReferenceDemo
 * @description 带有时间戳的对象引用
 */
public class AtomicStampedReferenceDemo {
    static AtomicStampedReference<Integer> money = new AtomicStampedReference<>(19, 0);

    public static void main(String[] args) {
        //模拟多个线程同时更新后台数据库,为用户充值
        int count = 3;
        final int moneyBalance = 20;
        final int moneyShop = 10;
        for (int i = 0; i < count; i++) {
            final int timestamp = money.getStamp();
            new Thread(){
                @Override
                public void run() {
                    for (; ; ) {
                        Integer leftMoney = money.getReference();
                        if (leftMoney < moneyBalance) {
                            if (money.compareAndSet(leftMoney, leftMoney + 20, timestamp, timestamp + 1)) {
                                System.out.println("余额小于20,充值成功,余额:" + money.getReference() + "元");
                                break;
                            }
                        } else {
                            System.out.println("余额大于20,无需充值");
                            break;
                        }
                    }
                }
            }.start();
        }

        //
        new Thread(){
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    for (; ; ) {
                        int stamp = money.getStamp();
                        Integer leftMoney = money.getReference();
                        if (leftMoney > moneyShop) {
                            System.out.println("余额充足");
                            if (money.compareAndSet(leftMoney, leftMoney - moneyShop, stamp, stamp + 1)) {
                                System.out.println("成功消费10元,余额:" + money.getReference());
                                break;
                            }
                        } else {
                            System.out.println("余额不足,请充值");
                            break;
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
