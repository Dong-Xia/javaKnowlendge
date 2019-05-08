package com.java.javaknowledge.service;

import com.java.javaknowledge.entity.ThreadA;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 熟悉ReentrantLock的相关使用
 */
public class ReentrantLockTest {
    private Lock lock = new ReentrantLock();

    public void testReentrantLock() {
        lock.lock();
        for (int i = 0;i < 5;i++){
            System.out.println(Thread.currentThread().getName() + " :"+ i);
        }
        lock.unlock();
    }

    public static void main(String[] args) {
        ReentrantLockTest reentrantLockTest = new ReentrantLockTest();
        ThreadA a1 = new ThreadA(reentrantLockTest);
        ThreadA a2 = new ThreadA(reentrantLockTest);
        ThreadA a3 = new ThreadA(reentrantLockTest);
        ThreadA a4 = new ThreadA(reentrantLockTest);
        ThreadA a5 = new ThreadA(reentrantLockTest);
        a1.start();
        a2.start();
        a3.start();
        a4.start();
        a5.start();
    }
}
