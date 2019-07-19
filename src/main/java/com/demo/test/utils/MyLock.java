package com.demo.test.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Jack
 * @date 2019/06/26 14:08 PM
 */
public class MyLock implements Lock {

    public LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();
    AtomicReference<Thread> owner = new AtomicReference<>();

    @Override
    public void lock() {
        while (!owner.compareAndSet(null, Thread.currentThread())) {
            waiters.add(Thread.currentThread());
            LockSupport.park();
            // 运行到这里，说明已经有线程被唤醒，可以去做事了
            waiters.remove(Thread.currentThread());
        }
    }

    @Override
    public void unlock() {
        if (owner.compareAndSet(Thread.currentThread(), null)) {
            Object[] objects = waiters.toArray();
            for (Object object : objects) {
                Thread next = (Thread) object;
                LockSupport.unpark(next);
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
