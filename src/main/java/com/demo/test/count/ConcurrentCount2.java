package com.demo.test.count;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 运行结果：100000000，耗时（ms) ：570 毫秒！
 */
public class ConcurrentCount2 {

    static AtomicInteger in = new AtomicInteger();

    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();
        for(int i = 0; i < 1000; i++) {
            for (int j = 0; j < 100000; j++) {
                in.incrementAndGet();
            }
        }
        long t2 = System.currentTimeMillis();

        System.out.println(String.format("运行结果：%s，耗时（ms) ：%s 毫秒！", in.get(), (t2 - t1)));
    }


}
