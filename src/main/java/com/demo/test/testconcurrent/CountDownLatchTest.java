package com.demo.test.testconcurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * ExecutorService 的实现类不适合CountDownLatch的使用，只能使用new Thread()
 */
@Slf4j
public class CountDownLatchTest {

    /**
     * 开始时间
     */
    private static long startTime = 0L;

    public static void main(String[] args) {
        CountDownLatchTest ct = new CountDownLatchTest();
        try {
            ct.waitToComplete();
        } catch (InterruptedException e) {
            log.error("ct.waitToComplete() method has exception : {} "+e.getMessage());
        } finally {
            System.exit(0);
        }
    }

    //@Test
    public void waitToComplete() throws InterruptedException {
        try {
            startTime = System.currentTimeMillis();
            log.info("CountDownLatch started at: " + startTime);
            // 初始化计数器为1
            CountDownLatch countDownLatch = new CountDownLatch(1);
            for (int i = 0; i < 100; i++) {
                new Thread(new CountDownLatchTest.FiniterThreadNamePrinter(countDownLatch)).start();
            }

            // 启动多个线程
            countDownLatch.countDown();
        } catch (Exception e) {
            log.error("waitToComplete method has exception : {} "+e.getMessage());
        }

        /*ExecutorService executor = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < 10; i++) {
            executor.execute(new CountDownLatchTest.FiniterThreadNamePrinter(latch));
        }
        latch.countDown();*/
    }

    private static class FiniterThreadNamePrinter implements Runnable {

        private final CountDownLatch latch;

        public FiniterThreadNamePrinter(CountDownLatch clatch) {
            this.latch = clatch;
        }

        @Override
        public final void run() {
            try {
                latch.await();
            } catch (InterruptedException e) {
                log.error("latch.await() has exception : {} " + e.getMessage());
            }
            for (int i = 0; i < 25; i++) {
                log.error("Run from Thread : "
                        + Thread.currentThread().getName() + " =====" + Thread.currentThread().getId());
            }
        }

    }
}
