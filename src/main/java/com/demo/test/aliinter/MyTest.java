package com.demo.test.aliinter;

import java.util.concurrent.LinkedBlockingQueue;

public class MyTest {
    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<>(10000);

        new MyProducer(queue).start();

        new MyCustomer(queue).start();

    }
}

