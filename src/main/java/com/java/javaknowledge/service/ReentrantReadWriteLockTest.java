package com.java.javaknowledge.service;

import com.java.javaknowledge.entity.ThreadD;
import com.java.javaknowledge.entity.ThreadE;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock读写锁实现
 */
public class ReentrantReadWriteLockTest {
    ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    /**
     * 测试读锁
     */
    public void testReadLock(){
        try {
            reentrantReadWriteLock.readLock().lock(); // 获取读锁（读锁为共享锁，资源共享），底层实现采用tryAcquireShared(int)其中负数表示失败；0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源
            System.out.println("获取读锁" + Thread.currentThread().getName() + "的时间:" + System.currentTimeMillis());
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
          reentrantReadWriteLock.readLock().unlock(); // 释放读锁（读锁为共享锁，资源共享），底层实现采用tryReleaseShared(int)，如果释放后允许唤醒后续等待结点返回true，否则返回false
        }
    }

    /**
     * 测试写锁
     */
    public void testWriteLock(){
        try {
            reentrantReadWriteLock.writeLock().lock(); // 获取写锁（写锁为互斥锁，资源独占）, 底层实现采用tryAcquire()成功则返回true，失败则返回false。
            System.out.println("获取写锁" + Thread.currentThread().getName() + "的时间:" + System.currentTimeMillis());
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reentrantReadWriteLock.writeLock().unlock(); // 释放写锁，底层调用tryRelease(int)：独占方式。尝试释放资源，成功则返回true，失败则返回false。
        }
    }

    public static void main(String[] args) {
        ReentrantReadWriteLockTest lock = new ReentrantReadWriteLockTest(); // 定义一个用于synchronized同步加锁的对象监视器（采用同步代码块的原理）
        ReentrantReadWriteLockTest reentrantReadWriteLockTest = new ReentrantReadWriteLockTest();
        try {
            synchronized (lock) {
                // 测试写读互斥和读写互斥，执行有先后
                System.out.println("---------测试读写互斥和写读互斥：开始----------------");
                testReadAndWriteLock(reentrantReadWriteLockTest);
                Thread.sleep(10000);
            }
            synchronized (lock) {
                // 测试写写互斥，执行有先后
                System.out.println("---------测试写写互斥：开始----------------");
                testWriteAndWriteLock(reentrantReadWriteLockTest);
                Thread.sleep(10000);
            }

            synchronized (lock) {
                // 测试读读共享，几乎是两个线程同时执行
                System.out.println("---------测试读读共享：开始----------------");
                testReadAndReadLock(reentrantReadWriteLockTest);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试读读共享
     * @param reentrantReadWriteLockTest
     */
    private static void testReadAndReadLock(ReentrantReadWriteLockTest reentrantReadWriteLockTest) {
        ThreadD d = new ThreadD(reentrantReadWriteLockTest);
        d.setName("A");
        ThreadD e = new ThreadD(reentrantReadWriteLockTest);
        e.setName("B");
        e.start();
        d.start();
    }

    /**
     * 测试写写互斥
     * @param reentrantReadWriteLockTest
     */
    private static void testWriteAndWriteLock(ReentrantReadWriteLockTest reentrantReadWriteLockTest) {
        ThreadE d = new ThreadE(reentrantReadWriteLockTest);
        d.setName("A");
        ThreadE e = new ThreadE(reentrantReadWriteLockTest);
        e.setName("B");
        e.start();
        d.start();
    }

    /**
     * 测试写读互斥和读写互斥
     * @param reentrantReadWriteLockTest
     */
    private static void testReadAndWriteLock(ReentrantReadWriteLockTest reentrantReadWriteLockTest) {
        ThreadD d = new ThreadD(reentrantReadWriteLockTest);
        d.setName("A");
        ThreadE e = new ThreadE(reentrantReadWriteLockTest);
        e.setName("B");
        e.start();
        d.start();
    }
}
