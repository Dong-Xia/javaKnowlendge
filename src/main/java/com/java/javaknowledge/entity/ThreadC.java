package com.java.javaknowledge.entity;

public class ThreadC extends Thread {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " : 执行开始");
        System.out.println(Thread.currentThread().getName() + " : 执行完毕");
    }
}
