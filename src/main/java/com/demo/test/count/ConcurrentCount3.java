package com.demo.test.count;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.LongAdder;

/**
 * 运行结果：100000000，耗时（ms) ：383 毫秒！
 */
public class ConcurrentCount3 {

    private static final int THREAD_COUNT = 1000;

    private static final int LOOP_COUNT = 100000;

    private static LongAdder counter = new LongAdder();

    public static void incr() {
        counter.increment();
    }

    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i < 10; i++) {
            counter.reset();
            m1();
        }
    }

    private static void m1() throws InterruptedException {
        long t1 = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);
        for(int i = 0; i < THREAD_COUNT; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < LOOP_COUNT; j++) {
                        incr();
                    }
                } finally {
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();

        long t2 = System.currentTimeMillis();
        System.out.println(String.format("运行结果：%s，耗时（ms) ：%s 毫秒！", counter.sum(), (t2 - t1)));
    }


}
