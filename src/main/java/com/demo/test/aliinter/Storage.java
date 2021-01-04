package com.demo.test.aliinter;

import java.util.concurrent.LinkedBlockingQueue;

public class Storage {
    // 仓库存储的载体
    private final LinkedBlockingQueue<Object> list = new LinkedBlockingQueue<>(10);

    public void produce() {
        try {
            this.list.put(new Object());
            System.out.println("【生产者" + Thread.currentThread().getName()
                    + "】生产一个产品，现库存" + this.list.size());
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void consume() {
        try {
            this.list.take();
            System.out.println("【消费者" + Thread.currentThread().getName()
                    + "】消费了一个产品，现库存" + this.list.size());
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
}
