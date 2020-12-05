package com.demo.test.concurrent;

/**
 * Java多线程任务超时结束的5种实现方法----方法一：使用Thread.join(long million)
 * 在主线程中等待t1执行2秒之后，要interrupt
 * （而不是直接调用stop，这个方法已经被弃用）掉它，然后在t1里面会产出一个中断异常，在异常里面处理完该处理的事，
 * 就要return，一定要return，如果不return的话，t1还会继续执行，只不过是与主线程并行执行。
 */
public class JoinTest {
    public static void main(String[] args) {
        Task task1 = new Task("one", 4);
        Task task2 = new Task("two", 2);
        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);
        t1.start();
        try {
            t1.join(2000); // 在主线程中等待t1执行2秒
        } catch (InterruptedException e) {
            System.out.println("t1 interrupted when waiting join");
            e.printStackTrace();
        }
        t1.interrupt(); // 这里很重要，一定要打断t1,因为它已经执行了2秒。
        t2.start();
        try {
            t2.join(1000);
        } catch (InterruptedException e) {
            System.out.println("t2 interrupted when waiting join");
            e.printStackTrace();
        }
    }
}

class Task implements Runnable {
    public String name;
    private int time;

    public Task(String s, int t) {
        name = s;
        time = t;
    }

    public void run() {
        for (int i = 0; i < time; ++i) {
            System.out.println("task " + name + " " + (i + 1) + " round");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(name
                        + " is interrupted when calculating, will stop...");
                return; // 注意这里如果不return的话，线程还会继续执行，所以任务超时后在这里处理结果然后返回
            }
        }
    }
}
