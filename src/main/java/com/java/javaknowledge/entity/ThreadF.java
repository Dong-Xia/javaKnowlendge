package com.java.javaknowledge.entity;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ThreadF extends Thread {
    private CyclicBarrier cyclicBarrier;
    private int i;

    public ThreadF(int i,CyclicBarrier cyclicBarrier){
        this.cyclicBarrier = cyclicBarrier;
        this.i = i;
    }

    @Override
    public void run() {
        try {
            if (i == 2) {
                // 模拟所有士兵等待士兵2
                Thread.sleep(10000);
                System.out.println("士兵" + i + "报到");
            }else {
                System.out.println("士兵" + i + "报到");
            }
            // 等待其他士兵
            cyclicBarrier.await();
            Thread.sleep(5000);
            // 等待所有士兵都报到后，该士兵可以进行自己的下一步动作
            if (i == 1) {
                System.out.println("士兵" + i + "体能训练");
            } else if (i == 2) {
                System.out.println("士兵" + i + "射击训练");
            } else {
                System.out.println("士兵" + i + "格斗训练");
            }
            // 模拟进行任务花费的时间
            Thread.sleep(3000);
            System.out.println("士兵" + i + "训练完成，请指示！");
            // 等待其他士兵完成训练任务
            cyclicBarrier.await(); // 栅栏可以进行重置后重新使用
            // System.out.println("所有士兵训练完成，请指示！"); 在这里打印的话，则每个士兵都会打印一次，但是这个命令在现实中应该是兵长来发出
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
