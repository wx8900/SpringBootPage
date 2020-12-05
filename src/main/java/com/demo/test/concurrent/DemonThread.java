package com.demo.test.concurrent;

import java.util.ArrayList;
import java.util.List;

/**
 * 方法四：设置一个守护线程，守护线程先sleep一段定时时间，睡醒后打断它所监视的线程
 * 一开始准备在守护任务里面用一个集合来实现监视多个任务，接着发现要实现这个功能还得
 * 在这个守护任务里面为每一个监视的任务开启一个监视任务，一时又想不到更好的方法来解决，
 * 索性只监视一个算了，留待以后改进吧。
 *
 * 方法五：使用Timer / TimerTask，或其他schedule定时相关的类
 *
 * 总结：需要注意的是，无论以上哪一种方法，其实现原理都是在超时后通过interrupt打断
 * 目标线程的运行，所以都要在捕捉到InterruptedException的catch代码块中return,
 * 否则线程仍然会继续执行。另外，最后两种方法本质上是一样的，都是通过持有目标线程的引用，
 * 在定时结束后打断目标线程，这两种方法的控制精度最低，因为它是采用另一个线程来监视目标线程的运行时间，
 * 因为线程调度的不确定性，另一个线程在定时结束后不一定会马上得到执行而打断目标线程。
 */
public class DemonThread {
    static class Task implements Runnable {
        private String name;
        private int time;

        public Task(String s, int t) {
            name = s;
            time = t;
        }

        public int getTime() {
            return time;
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

    static class Daemon implements Runnable {
        List<Runnable> tasks = new ArrayList<Runnable>();
        private Thread thread;
        private int time;

        public Daemon(Thread r, int t) {
            thread = r;
            time = t;
        }

        public void addTask(Runnable r) {
            tasks.add(r);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(time * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                thread.interrupt();
            }
        }

    }

    public static void main(String[] args) {
        Task task1 = new Task("one", 5);
        Thread t1 = new Thread(task1);
        Daemon daemon = new Daemon(t1, 3);
        Thread daemoThread = new Thread(daemon);
        daemoThread.setDaemon(true);
        t1.start();
        daemoThread.start();
    }
}
