package com.demo.test.testing;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CountDownLatchTest {

    public static void main(String[] args) {
        CountDownLatchTest ct = new CountDownLatchTest();
        try {
            ct.waitToComplete();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    //@Test
    public void waitToComplete() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);
        executor.execute(new FiniterThreadNamePrinterLatch(latch));
        latch.await(5, TimeUnit.SECONDS);
    }

    private static class FiniterThreadNamePrinterLatch implements Runnable {

        CountDownLatch latch;

        public FiniterThreadNamePrinterLatch(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public final void run() {
            for (int i = 0; i < 25; i++) {
                System.out.println("Run from Thread : "
                        + Thread.currentThread().getName() + " =====" + Thread.currentThread().getId());
            }
            latch.countDown();
        }

    }
}
