package com.demo.test.testing;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainTest {

    public static void main(String[] args) throws InterruptedException {
        long begaintime = System.currentTimeMillis();//开始系统时间
        //线程池
        ExecutorService pool = Executors.newCachedThreadPool();
        //设置集合点为93
        final int count = 50;
        CountDownLatch countDownLatch = new CountDownLatch(count);//与countDownLatch.await();实现运行完所有线程之后才执行后面的操作
        //final CyclicBarrier barrier = new CyclicBarrier(count);  //与barrier.await() 实现并发;
        //创建100个线程
        for (int i = 0; i < count; i++) {
            MyThread target = new MyThread("para1", "para2", "para3", countDownLatch);
            //barrier.await();
            pool.execute(target);
        }
        pool.shutdown();
        try {
            countDownLatch.await();  //这一步是为了将全部线程任务执行完以后，开始执行后面的任务（计算时间，数量）
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis(); //结束时间
        System.out.println(count + " 个  接口请求总耗时 ： " + (endTime - begaintime) + "-----平均耗时为" + ((endTime - begaintime) / count));
    }

    /*public static void main(String[] args) {
        String Url = "http://localhost:8085/test/add";
        RunThread testMain = new RunThread(Url, 100);

        // 测试Get请求
        testMain.testGet(new HashMap<>());
    }*/
}
