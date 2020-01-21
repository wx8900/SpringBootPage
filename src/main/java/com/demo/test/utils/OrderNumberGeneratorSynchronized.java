package com.demo.test.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 全局订单ID——Synchronized
 */
public class OrderNumberGeneratorSynchronized implements OrderNumberGeneratorInterface {

    public static int count = 0;
    public static Object lock = new Object();

    @Override
    public String getNumber() {
        synchronized (lock) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String s = simpleDateFormat.format(new Date()) + "-" + FileId.nextId();
        }
        return null;
    }
}
