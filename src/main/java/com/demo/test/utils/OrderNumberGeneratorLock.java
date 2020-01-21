package com.demo.test.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 全局订单ID——Lock
 */
public class OrderNumberGeneratorLock implements OrderNumberGeneratorInterface {

    public static int count = 0;
    private Lock lock = new ReentrantLock();

    @Override
    public String getNumber() {
        SimpleDateFormat simpleDateFormat;
        try {
            lock.lock();
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String s = simpleDateFormat.format(new Date()) + "-" + FileId.nextId();
            return s;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        OrderNumberGeneratorLock og = new OrderNumberGeneratorLock();
        System.out.println(og.getNumber());
    }

}
