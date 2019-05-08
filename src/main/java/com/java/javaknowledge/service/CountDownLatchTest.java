package com.java.javaknowledge.service;

import com.java.javaknowledge.entity.ThreadC;

import java.util.concurrent.CountDownLatch;

/**
 * 学习countDownLatch相关知识
 */
public class CountDownLatchTest {
    private CountDownLatch countDownLatch = new CountDownLatch(3); //初始化state为3个线程的大小，利用它可以实现类似计数器的功能。比如有一个任务A，它要等待其他3个任务执行完毕之后才能执行

    public void testCountDownLatch(){
            try {
                for (int i = 0;i < 3;i++) {
                    ThreadC a = new ThreadC();
                    a.start();
                    Thread.sleep(3000);
                    countDownLatch.countDown(); // 每次一个线程执行完毕则进行state减1
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        try {
            // 主线程等待计数为0后，再启动执行
            System.out.println("等待3个子线程执行完毕...");
            countDownLatch.await();
            System.out.println("3个子线程已经执行完毕");
            System.out.println("继续执行主线程");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new CountDownLatchTest().testCountDownLatch();
        System.out.println(Thread.currentThread().getName() + "主线程当前时间:" + System.currentTimeMillis());
    }
}
