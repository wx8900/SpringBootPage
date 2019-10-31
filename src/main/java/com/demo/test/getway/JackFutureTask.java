package com.demo.test.getway;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.LockSupport;

public class JackFutureTask<T> implements Runnable {
    Callable<T> callable;

    T result;

    BlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    volatile String state = "NEW";

    public JackFutureTask(Callable<T> callable) {
        this.callable = callable;
    }

    public T get() {
        if ("END".equals(state)) {
            return result;
        }
        // 把get()放入容器
        waiters.add(Thread.currentThread());

        while (!"END".equals(state)) {
            LockSupport.park(); // 阻塞,等待unpark()
        }
        //阻塞到有返回值为止
        return result;
    }

    @Override
    public void run() {
        try {
            result = callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            state = "END";
        }

        Thread waiter = waiters.poll();
        while (waiter != null) {
            LockSupport.unpark(waiter);
            waiter = waiters.poll();
        }
    }
}
