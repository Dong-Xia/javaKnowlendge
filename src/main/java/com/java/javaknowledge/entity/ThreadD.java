package com.java.javaknowledge.entity;

import com.java.javaknowledge.service.ReentrantReadWriteLockTest;

public class ThreadD extends Thread {
    private ReentrantReadWriteLockTest reentrantReadWriteLockTest;

    public ThreadD(ReentrantReadWriteLockTest reentrantReadWriteLockTest){
        this.reentrantReadWriteLockTest = reentrantReadWriteLockTest;
    }

    @Override
    public void run() {
        reentrantReadWriteLockTest.testReadLock();
    }
}
