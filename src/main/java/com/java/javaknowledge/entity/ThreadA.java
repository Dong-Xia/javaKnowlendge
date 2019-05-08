package com.java.javaknowledge.entity;

import com.java.javaknowledge.service.ReentrantLockTest;

public class ThreadA extends Thread{
    private ReentrantLockTest reentrantLockTest;
    public ThreadA(ReentrantLockTest reentrantLockTest){
        this.reentrantLockTest = reentrantLockTest;
    }

    public void run(){
        reentrantLockTest.testReentrantLock();
    }
}
