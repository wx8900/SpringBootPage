package com.demo.test.testing;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConCurrentTest {

    public static void main(String[] args) {
        int count = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        for (int i = 0; i < count; i++) {
            //executorService.execute(new Test().new Task());
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
