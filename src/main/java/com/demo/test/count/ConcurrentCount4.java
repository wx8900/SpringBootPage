package com.demo.test.count;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.LongAccumulator;

/**
 * 运行结果：100000000，耗时（ms) ：287 毫秒！
 */
public class ConcurrentCount4 {

    static LongAccumulator counter = new LongAccumulator((x, y) -> x + y, 0L);

    private static final int THREAD_COUNT = 1000;

    private static final int LOOP_COUNT = 100000;

    public static void incr() {
        counter.accumulate(1);
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
        System.out.println(String.format("运行结果：%s，耗时（ms) ：%s 毫秒！",
                counter.longValue(), (t2 - t1)));
    }

}
