# 构建锁和同步器的框架：AbstractQueuedSynchronizer（AQS）
## 概述
AbstractQueuedSynchronizer（AQS）是一个用于构建锁和同步器的框架，许多同步器都可以通过它很容易并且高效地构造出来，常用到的ReentrantLock、Semaphore、CountDownLatch、ReentrantReadWriteLock都是
基于AQS进行构建的。
## 框架
![AQS](/src/main/images/AQS-抽象队列式同步器.jpg)

类如其名，抽象的队列式的同步器，它维护了一个volatile int state（代表共享资源）和一个FIFO线程等待队列（多线程争用资源被阻塞时会进入此队列）。这里volatile是核心关键词，volatile作用为保持变量的可见性。state的访问方式有三种:
                
    （１）getState()
    （２）setState()
    （３）compareAndSetState()

　　AQS定义了两种获取资源的方式：Exclusive独占（只有一个线程执行，如ReentrantLock）和Share共享(可以多个线程同时执行，如Semaphore、CountDownLatch)。

　　不同的自定义同步器争用共享资源的方式也不同。**自定义同步器在实现时只需要实现共享资源state的获取与释放方式即可,即采用独占和共享方式**，至于具体线程等待队列的维护（如获取资源失败入队/唤醒出队等），AQS已经在顶层实现好了。自定义同步器实现时主要实现以下几种方法：
- isHeldExclusively()：该线程是否正在独占资源。只有用到condition才需要去实现它。
- tryAcquire(int)：独占方式。尝试获取资源，成功则返回true，失败则返回false。
- tryRelease(int)：独占方式。尝试释放资源，成功则返回true，失败则返回false。
- tryAcquireShared(int)：共享方式。尝试获取资源。负数表示失败；0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源。
- tryReleaseShared(int)：共享方式。尝试释放资源，如果释放后允许唤醒后续等待结点返回true，否则返回false。

　　一般来说，自定义同步器要么是独占方法，要么是共享方式，他们也只需实现tryAcquire-tryRelease、tryAcquireShared-tryReleaseShared中的一种即可。但AQS也支持自定义同步器同时实现独占和共享两种方式，如ReentrantReadWriteLock。
#### 举例说明：
    
1. ReentrantLock实现为独占方式：state的初始状态为0，当线程A进行访问时，调用lock()方法时，**方法内部代码将调用tryAcquire()独占资源并进行加锁后
    将state+1**；其它线程访问共享资源时调用tryAcquire()则会失效，直到线程A**调用unlock()方法释放锁后（方法内部通过实现tryRelease()以独占方式释放资源）**，
    其它线程才能有机会获取该锁。 值得注意的是，在线程A释放锁之前，线程A可以重复获取锁（可重入锁的概念），相应的state也要进行累加，
    但是，必须注意的是锁获取了多少次则需要释放多少次，不然state就不能恢复到0状态。
         
         代码实例：
         （1）、定义线程需要获取锁并执行的资源类
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
          （2）、定义线程类
              public class ThreadA extends Thread{
                  private ReentrantLockTest reentrantLockTest;
                  public ThreadA(ReentrantLockTest reentrantLockTest){
                      this.reentrantLockTest = reentrantLockTest;
                  }
              
                  public void run(){
                      reentrantLockTest.testReentrantLock();
                  }
              }
2. Semaphore实现为共享方式：Semaphore 是 synchronized 的加强版，作用是控制线程的并发数量；就这一点而言，单纯的synchronized 关键字是实现不了的。
**初始化state的状态为允许线程并发的数量，表示在同一个时刻，只运行多少个进程同时运行指定的代码**。

　　具体实现分两步：
- Semaphore调用acquire()获取共享锁，底层实现调用tryReleaseShared()以共享的方式释放锁，相应共享状态state+1，
只要state状态不为0，则其它线程可以获取锁，并获得资源；
- Semaphore调用release()释放共享锁，底层实现调用tryReleaseShared()以共享的方式释放锁，相应共享状态state+1，
**只要state状态不为0，则其它线程可以获取锁，并获得资源**。
        
        代码实现：
        （1）、定义线程需要获取锁并执行的资源类
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
        （2）、定义线程类
            public class ThreadB extends Thread {
                private SemaphoreTest semaphoreTest;
                public ThreadB(SemaphoreTest semaphoreTest){
                    this.semaphoreTest = semaphoreTest;
                }
            
                public void run(){
                    semaphoreTest.semaphoreTest();
                }
            }
3. CountDownLatch实现方式为共享方式：再以CountDownLatch以例，任务分为N个子线程去执行，state也初始化为N（注意N要与线程个数一致）。
这N个子线程是并行执行的，每个子线程执行完后countDown()一次，state会CAS减1。等到所有子线程都执行完后(即state=0)，会unpark()主调用线程，
然后主调用线程就会从await()函数返回，继续后余动作。
    
        代码实现：
        （1）、定义线程需要获取锁并执行的资源类
        public class CountDownLatchTest {
            private CountDownLatch countDownLatch = new CountDownLatch(3); //初始化state为3个线程的大小，利用它可以实现类似计数器的功能。比如有一个任务A，它要等待其他3个任务执行完毕之后才能执行
        
            public void testCountDownLatch(){
                    try {
                        for (int i = 0;i < 3;i++) {
                            ThreadC a = new ThreadC();
                            a.start();
                            Thread.sleep(3000);
                            countDownLatch.countDown(); // 每次一个线程执行完毕则进行state减1
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
        
                try {
                    // 主线程等待计数为0后，再启动执行
                    System.out.println("等待3个子线程执行完毕...");
                    countDownLatch.await();
                    System.out.println("3个子线程已经执行完毕");
                    System.out.println("继续执行主线程");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        
            public static void main(String[] args) {
                new CountDownLatchTest().testCountDownLatch();
                System.out.println(Thread.currentThread().getName() + "主线程当前时间:" + System.currentTimeMillis());
            }
        }
       （2）、定义线程类
           public class ThreadC extends Thread {
           
               @Override
               public void run() {
                   System.out.println(Thread.currentThread().getName() + " : 执行开始");
                   System.out.println(Thread.currentThread().getName() + " : 执行完毕");
               }
           }
4. ReentrantReadWriteLock同时实现独占和共享两种方式：主要分为读写互斥、写读互斥、写写互斥和读读共享三种情况。

        代码实现：
       （1）定义线程需要获取锁并执行的资源类
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
       （2）、定义线程类
       读线程：
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
       
       写线程：
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