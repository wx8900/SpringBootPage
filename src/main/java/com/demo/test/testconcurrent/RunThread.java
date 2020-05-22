package com.demo.test.testconcurrent;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 高并发代码吧，经过实验，通过1000个并发请求，使用Durid + Lock成功1百个不到
 * 使用dbcp2 + Lock成功2百多个，使用dbcp2 + synchronized 竟然成功了940个。
 *
 * @Autowired private TestMapper testMapper;
 * //private Lock lock = new ReentrantLock();
 * public synchronized Integer test(Integer a, Integer b) {
 * int c = testMapper.select();
 * c += 1;
 * update(c);
 * return c;
 * }
 * @Transactional(isolation = Isolation.SERIALIZABLE)
 * public void update(int c) {
 * testMapper.insert(c);
 * }
 */
public class RunThread {

    private static ThreadPoolExecutor executor =
            new ThreadPoolExecutor(10, 100,
                    1000000L, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
    private final String URL;
    private OkHttpClientUtils okHttpClientUtils;
    private int num;
    private CountDownLatch countDownLatch;

    /**
     * @param url 服务URL地址，
     * @param num 并发访问次数，一般配置50+
     */
    public RunThread(String url, int num) {
        this.URL = url;
        this.num = num;
        this.countDownLatch = new CountDownLatch(num);

        okHttpClientUtils = new OkHttpClientUtils() {
            @Override
            public String callBack(String responseString) {
                System.out.println(responseString);
                return responseString;
            }
        };
    }

    public void testGet(Map<Object, Object> map) {

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < num; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    okHttpClientUtils.get(URL, map);
                    countDownLatch.countDown();
                }
            });
        }

        try {
            countDownLatch.await();
            long executeTime = System.currentTimeMillis() - startTime;
            System.out.println("一共消耗：" + executeTime + "毫秒");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public <T> void testPost(Map<Object, Object> map, T t) {

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < num; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    okHttpClientUtils.post(URL, map, t);
                    countDownLatch.countDown();
                }
            });
        }

        try {
            countDownLatch.wait();
            long executeTime = System.currentTimeMillis() - startTime;
            System.out.println("一共消耗：" + executeTime + "毫秒");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
