package com.java.javaknowledge.entity;

import com.java.javaknowledge.service.ReentrantReadWriteLockTest;

public class ThreadE extends Thread {
    private ReentrantReadWriteLockTest reentrantReadWriteLockTest;

    public ThreadE(ReentrantReadWriteLockTest reentrantReadWriteLockTest){
        this.reentrantReadWriteLockTest = reentrantReadWriteLockTest;
    }

    @Override
    public void run() {
        reentrantReadWriteLockTest.testWriteLock();
    }
}
