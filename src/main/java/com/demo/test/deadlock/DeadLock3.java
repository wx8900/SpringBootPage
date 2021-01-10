package com.demo.test.deadlock;

public class DeadLock3 implements Runnable {

    public static void main(String[] args) {
        DeadLock3 d31 = new DeadLock3();
        DeadLock3 d32 = new DeadLock3();
        d31.flag = 1;
        d32.flag = 0;
        new Thread(d31).start();
        new Thread(d32).start();
    }

    public int flag = 0;
    private static Object o1 = new Object(), o2 = new Object();

    @Override
    public void run() {
        if (flag == 1) {
            synchronized (o1) {
                try {
                    Thread.sleep(1000);
                    System.out.println("flag is 1, lock o1 object!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o2) {
                    System.out.println("flag is 1, lock o2 object!");
                }
            }
        }
        if (flag == 0) {
            synchronized (o2) {
                try {
                    Thread.sleep(1000);
                    System.out.println("flag is 0, lock o2 object!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o1) {
                    System.out.println("flag is 0, lock o1 object!");
                }
            }
        }
    }
}
