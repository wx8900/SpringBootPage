package com.demo.test.aliinter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class BatchProviderCustomer {
    //任务队列
    private static BlockingQueue<Task> bq = new LinkedBlockingQueue<>(2000);

    public static void main(String[] args) {
        start();
    }

    //启动5个消费者线程
    //执行批量任务
    static void start() {
        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            es.execute(() -> {
                try {
                    while (true) {
                        //获取批量任务
                        List<Task> ts = pollTasks();
                        //执行批量任务
                        execTasks(ts);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    //批量执行任务
    private static void execTasks(List<Task> ts) {
        //省略具体代码无数
    }

    //从任务队列中获取批量任务
    private static List<Task> pollTasks() throws InterruptedException {
        List<Task> ts = new LinkedList<>();
        //阻塞式获取一条任务
        Task t = null;
        try {
            t = bq.take();
            System.out.println("从任务队列中获取批量任务");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (t != null) {
            ts.add(t);
            //非阻塞式获取一条任务
            t = bq.poll();
        }
        return ts;
    }
}

class Task {

}
