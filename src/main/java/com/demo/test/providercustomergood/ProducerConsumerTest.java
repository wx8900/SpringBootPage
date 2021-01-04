package com.demo.test.providercustomergood;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 基于链表的阻塞队列,内部也维持着一个数据缓冲队列,该队列由一个链表构成
 * 当生产者往队列中放入一个数据，队列会从生产者手中获取数据,并缓存在队列内部，生产者返回。
 * 只有当队列缓冲区达到最大值缓存容量时才会阻塞生产者队列，直到消费者从队列中消费掉一份数据，生产者线程会被唤醒，
 * 反之对于消费者这端的处理也基于同样的原理。
 * 而LinkedBlockingQueue之所以能够高效的处理并发数据，还因为其对于生产者端和消费者端分别采用了独立的锁来控制数据同步
 * 这也意味着在高并发的情况下生产者和消费者可以并行地操作队列中的数据以此来提高整个队列的并发性能。
 * 在它内部, take和put操作本身是隔离的, 有若干个元素的时候, 一个在queue的头部操作, 一个在queue的尾部操作, 因此分别持有一把独立的锁。
 * 作为开发者，我们需要注意的是，如果构造LinkedBlockingQueue对象，没有指定其容量大小，
 * LinkedBlockingQueue默认一个的容量Integer.MAX_VALUE=2 ^31 - 1 = 2147483647
 * 如果生产者的速度一旦大于消费者的速度，也许还没有等到队列满阻塞产生，系统内存就有可能已被消耗殆尽了。
 */
public class ProducerConsumerTest {
    public static void main(final String[] args) {
        //如果这里没有写2，就默认为最大，要避免出现这种情况。
        final BlockingQueue<String> queue = new LinkedBlockingQueue<>(2);
        final Producer p = new Producer(queue);
        final Consumer c = new Consumer(queue);
        for (int i = 1; i <= 5; i++) {
            new Thread(p, "产品" + i).start();
            new Thread(c, "消费者 " + i).start();
        }
    }
}
