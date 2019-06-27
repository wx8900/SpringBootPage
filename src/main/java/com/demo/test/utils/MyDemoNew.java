package com.demo.test.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class MyDemoNew {
    //int i = 0;
    AtomicInteger i = new AtomicInteger(0);

    public void increase() {
        //i++;
        i.getAndIncrement();
    }

    public static void main(String[] args) throws Exception {
        MyDemoNew demoNew = new MyDemoNew();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread());
                for (int j = 0; j < 10000; j++) {
                    demoNew.increase();
                }
            }
            ).start();
        }
        Thread.sleep(1000L);
        System.out.println(demoNew.i);
    }
}
