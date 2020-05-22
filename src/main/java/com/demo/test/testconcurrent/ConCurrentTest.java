package com.demo.test.testconcurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
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
                log.error("Thread.sleep() has exception", e.getMessage());
            }
        }
    }

}
