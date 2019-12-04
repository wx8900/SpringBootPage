package com.demo.test.testconcurrent;

import java.util.concurrent.CountDownLatch;

/**
 * 测试工具类
 *
 * @author Jack
 * @date 2019/10/08
 */
public class MutilThreadTest {

    public static void main(String[] args) {
        int count = 100;
        final CountDownLatch cdl = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    cdl.countDown();
                    try {
                        cdl.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        ConnectionUtil.connect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    /*public static void main(String[] args) {
        int count = 100;
        final CountDownLatch cdl = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        cdl.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        ConnectionUtil.connect();
                        //GetDataFromAPI.json();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    cdl.countDown();
                }
            }).start();
        }
    }*/

}
