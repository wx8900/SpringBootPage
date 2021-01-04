package com.demo.test.aliinter;

import java.util.concurrent.LinkedBlockingQueue;

public class MyProducer<T> extends Thread {

    LinkedBlockingQueue<Object> queue;

    public MyProducer(LinkedBlockingQueue<Object> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 1000; i++) {
                queue.put(new String("new message " + i));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
