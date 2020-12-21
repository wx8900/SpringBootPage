package com.demo.test.count;

import java.util.concurrent.atomic.LongAdder;

/**
 * 运行结果：100000000，耗时（ms) ：1233 毫秒！
 * 创建10000个线程，每个线程计数10万
 */
public class ConcurrentCount1 {

    private static final int THREAD_COUNT = 1000;

    private static final int LOOP_COUNT = 100000;

    private static LongAdder inc = new LongAdder();

    public static void incr() {
        inc.increment();
    }

    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i < 10; i++) {
            inc.reset();
            count();
        }
    }

    public static void count() throws InterruptedException {
        final ConcurrentCount1 test = new ConcurrentCount1();
        Thread th;
        long t1 = System.currentTimeMillis();
        for(int i = 0; i < THREAD_COUNT; i++) {
            th = new Thread() {
              @Override
              public void run() {
                  for (int j = 0; j < LOOP_COUNT; j++) {
                      incr();
                  }
              }
            };
            th.start();;
            th.join();
        }

        long t2 = System.currentTimeMillis();

        System.out.println(String.format("运行结果：%s，耗时（ms) ：%s 毫秒！", inc.sum(), (t2 - t1)));
    }

}
