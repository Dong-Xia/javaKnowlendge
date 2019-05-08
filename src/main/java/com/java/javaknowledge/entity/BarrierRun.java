package com.java.javaknowledge.entity;

/**
 * 初始化CyclicBarrier使用
 */
public class BarrierRun implements Runnable {
    private boolean flag;
    private int N;

    public BarrierRun(boolean flag,int N){
        this.flag = flag;
        this.N = N;
    }

    @Override
    public void run() {
        if(flag){
            System.out.println("兵长:[士兵"+N+"个，训练任务完成！]");
        }else{
            System.out.println("兵长:[士兵"+N+"个，集合完毕！]");
            flag = true;
        }
    }
}
