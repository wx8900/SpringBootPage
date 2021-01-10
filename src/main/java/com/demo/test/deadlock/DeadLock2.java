package com.demo.test.deadlock;

public class DeadLock2 implements Runnable {

    public static void main(String[] args) {
        DeadLock2 d1 = new DeadLock2();
        DeadLock2 d2 = new DeadLock2();
        d1.flag = 1;
        d2.flag = 0;
        new Thread(d1, "A Thread").start();
        new Thread(d2, "B Thread").start();
    }

    public int flag;
    private static Object o1 = new Object(), o2 = new Object();

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() +" flag = " + flag);
        if (flag == 1) {
            synchronized(o1) {
                System.out.println(Thread.currentThread().getName() + " lock o1.");
                try {
                    Thread.sleep(200);
                    System.out.println(Thread.currentThread().getName() + " is waiting for unlock o2.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o2) {
                    System.out.println(Thread.currentThread().getName() + " lock o2.");
                }
            }
        }
        if (flag == 0) {
            synchronized(o2) {
                System.out.println(Thread.currentThread().getName() + " lock o2.");
                try {
                    Thread.sleep(200);
                    System.out.println(Thread.currentThread().getName() + " is waiting for unlock o1.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o1) {
                    System.out.println(Thread.currentThread().getName() + " lock o1.");
                }
            }
        }
    }
}
