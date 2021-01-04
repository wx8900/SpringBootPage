package com.demo.test.providercustomergood;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
    BlockingQueue<String> queue;

    public Producer(final BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            final String temp = Thread.currentThread().getName();
            System.out.println("生产者生产了 : " + Thread.currentThread().getName());
            this.queue.put(temp);//生产者传递消息给消费者
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
