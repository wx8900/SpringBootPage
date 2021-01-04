package com.demo.test.aliinter;

import java.util.concurrent.LinkedBlockingQueue;

public class MyCustomer<T> extends Thread {

    LinkedBlockingQueue<Object> queue;

    public MyCustomer(LinkedBlockingQueue<Object> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (queue.size() != 0) {
            try {
                Object res = queue.take();
                System.out.println(res);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
