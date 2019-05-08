package com.java.javaknowledge.service;

import com.java.javaknowledge.entity.ThreadA;
import com.java.javaknowledge.entity.ThreadB;

import java.util.concurrent.Semaphore;

/**
 * 练习使用Semaphore的相关内容
 */
public class SemaphoreTest {
    private Semaphore semaphore = new Semaphore(2); // 同步关键类，构造方法传入的数字是多少，则同一个时刻，只运行多少个进程同时运行指定的代码，例如：代码中相当于定义初始化state为2

    public void semaphoreTest(){
        try {
            semaphore.acquire(); // 获取共享锁，底层实现调用tryAcquireShared()以共享的方式获取锁，进入后相应共享状态state-1，当state为0时，其它线程将不能获取锁，只能等待占有锁的线程释放锁后才能有机会获取锁和资源
            for (int i = 0;i < 5;i++){
                System.out.println(Thread.currentThread().getName() + " :"+ i);
            }
            semaphore.release(); // 释放共享锁，底层实现调用tryReleaseShared()以共享的方式释放锁，相应共享状态state+1，只要state状态不为0，则其它线程可以获取锁，并获得资源
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SemaphoreTest semaphoreTest = new SemaphoreTest();
        ThreadB a1 = new ThreadB(semaphoreTest);
        ThreadB a2 = new ThreadB(semaphoreTest);
        ThreadB a3 = new ThreadB(semaphoreTest);
        ThreadB a4 = new ThreadB(semaphoreTest);
        ThreadB a5 = new ThreadB(semaphoreTest);
        a1.start();
        a2.start();
        a3.start();
        a4.start();
        a5.start();
    }
}
