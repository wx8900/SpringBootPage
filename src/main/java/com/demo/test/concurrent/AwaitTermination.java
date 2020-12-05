package com.demo.test.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 方法三：ExecutorService.awaitTermination(long million, TimeUnit unit)
 * 这个方法会一直等待所有的任务都结束，或者超时时间到立即返回，若所有任务都完成则返回true，否则返回false
 */
public class AwaitTermination {
    static class Task implements Runnable {
        public String name;
        private int time;

        public Task(String s, int t) {
            name = s;
            time = t;
        }

        public void run() {
            for (int i = 0; i < time; ++i) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(name
                            + " is interrupted when calculating, will stop...");
                    return; // 注意这里如果不return的话，线程还会继续执行，所以任务超时后在这里处理结果然后返回
                }
                System.out.println("task " + name + " " + (i + 1) + " round");
            }
            System.out.println("task " + name + " finished successfully");
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Task task = new Task("one", 5);
        Task task2 = new Task("two", 2);
        Future<?> future = executor.submit(task);
        Future<?> future2 = executor.submit(task2);
        List<Future<?>> futures = new ArrayList<Future<?>>();
        futures.add(future);
        futures.add(future2);
        try {
            if (executor.awaitTermination(3, TimeUnit.SECONDS)) {
                System.out.println("task finished");
            } else {
                System.out.println("task time out,will terminate");
                for (Future<?> f : futures) {
                    if (!f.isDone()) {
                        f.cancel(true);
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println("executor is interrupted");
        } finally {
            executor.shutdown();
        }
    }
}
