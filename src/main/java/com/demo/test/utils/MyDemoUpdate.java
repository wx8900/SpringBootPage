package com.demo.test.utils;

import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;

public class MyDemoUpdate {
    int i = 0;
    //Lock lock = new ReentrantLock();
    Lock lock = new MyLock();

    public static void main(String[] args) throws Exception {
        MyDemoUpdate myDemoUpdate = new MyDemoUpdate();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread());
                for (int j = 0; j < 10000; j++) {
                    myDemoUpdate.increase();
                }
            }
            ).start();
        }
        Thread.sleep(1000L);
        System.out.println(myDemoUpdate.i);
    }

    public void increase() {
        lock.lock();
        try {
            i++;
        } finally {
            lock.unlock();
        }
    }
}
