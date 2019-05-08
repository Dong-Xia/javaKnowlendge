package com.java.javaknowledge.service;

import com.java.javaknowledge.entity.BarrierRun;
import com.java.javaknowledge.entity.ThreadF;

import java.util.concurrent.CyclicBarrier;

/**
 * 同步工具类-CyclicBarrier栅栏
 */
public class CyclicBarrierTest {
    private CyclicBarrier cyclicBarrier = new CyclicBarrier(10); // 初始化state大小为线程的数量，10个线程需相互等待，所有线程需全部达到栅栏位置，才能继续执行；闭锁用于等待事件，栅栏用于等待线程。

    /**
     * 实现栅栏所有线程互相等待，等所有线程到齐后，再各自执行后面的任务
     */
    public void testCyclicBarrier(){
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10); // 初始化state大小为线程的数量，10个线程需相互等待，所有线程需全部达到栅栏位置，才能继续执行；闭锁用于等待事件，栅栏用于等待线程。
        for(int i = 1;i <= 10;i++){
                ThreadF threadF = new ThreadF(i,cyclicBarrier);
                threadF.start();
            }
    }

    /**
     * CyclicBarrier的另一个构造函数CyclicBarrier(int parties, Runnable barrierAction)，用于线程到达屏障时，优先执行barrierAction，方便处理更复杂的业务场景。
     * Runnable barrierAction 参数作用类似于：所有士兵集合完毕后，由兵长来发出“所有士兵集合完成”命令，对所有线程达到栅栏屏障后优先执行一个指定的共同行为方法
     */
     public void testCyclicBarrierAction(){
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10,new BarrierRun(false,10)); // 可以在所有线程到达栅栏屏障后，优先执行一个指定的共同方法，比如：所有士兵集合完毕后，发出“所有士兵集合完成”命令，如果采用CyclicBarrier(int parties)则会出现，有10个士兵则会打印出10次命令。
         for(int i = 1;i <= 10;i++){
             ThreadF threadF = new ThreadF(i,cyclicBarrier);
             threadF.start();
         }
     }
    public static void main(String[] args) {
        new CyclicBarrierTest().testCyclicBarrierAction();
    }
}
