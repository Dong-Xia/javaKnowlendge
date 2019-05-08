package com.java.javaknowledge.entity;

import com.java.javaknowledge.service.SemaphoreTest;

public class ThreadB extends Thread {
    private SemaphoreTest semaphoreTest;
    public ThreadB(SemaphoreTest semaphoreTest){
        this.semaphoreTest = semaphoreTest;
    }

    public void run(){
        semaphoreTest.semaphoreTest();
    }
}
