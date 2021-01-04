package com.demo.test.providercustomergood;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
    BlockingQueue<String> queue;

    public Consumer(final BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            //Thread.sleep(1);
            final String temp = this.queue.take();//消费者接受生产者的消息
            System.out.println(Thread.currentThread().getName() +  " 消费了 ： " + temp);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
