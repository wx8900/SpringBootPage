package com.demo.test.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jack
 * @version 2.0
 * @date 2019/06/22 01:24 AM
 * @date 2019/06/26 13:42 PM
 * @date 2019/10/17 17:32 PM
 */
public class TinyURLUtils {

    private static final String BASE_HOST = "http://tinyurl.com/";
    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int K = 6;
    private static final int SIZE62 = BASE62.length();
    /**
     * private static final AtomicLong atomicLong = new AtomicLong();
     */
    private static final ConcurrentHashMap<String, String> shortUrlMapping = new ConcurrentHashMap<>();
    static Logger logger = LogManager.getLogger("TinyURLUtils");
    Lock lock = new ReentrantLock();
    //Lock lock = new MyLock();

    public static void main(String[] args) throws Exception {
        TinyURLUtils tinyURLUtils = new TinyURLUtils();
        String encodeString0 = "https://www.youtube.com/watch?v=8aG3Z62rfghk";
        String encodeString1 = "https://www.youtube.com/watch?v=8aG3Z62rfdsa";
        String encodeString2 = "https://www.youtube.com/watch?v=8aG3Z611111";
        String[] buffer = new String[]{encodeString0, encodeString1, encodeString2};

        // 3个线程同时跑
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            new Thread(() -> {
                //for (int j = 0; j < 5; j++) {
                    String shURL = tinyURLUtils.encode(buffer[finalI]);
                    tinyURLUtils.decode(shURL);
                //}
            }
            ).start();
        }
        Thread.sleep(1000L);
    }

    /**
     * Encodes a URL to a shortened URL
     *
     * @param longUrl
     * @return
     */
    public String encode(String longUrl) {
        if (longUrl == null || longUrl.trim().length() <= 0) {
            return longUrl;
        }
        /** String uniqueURL = String.valueOf(atomicLong.getAndIncrement()); */
        /** encodedToUrl.put(uniqueURL, longUrl); */
        lock.lock();
        String shortUrl = "";
        try {
            if ("".equals(shortUrl)) {
                shortUrl = generateRandomShortUrl();
            }
            while (shortUrlMapping.containsKey(shortUrl)) {
                shortUrl = generateRandomShortUrl();
            }
            shortUrlMapping.put(shortUrl, longUrl);
        } catch (Exception e) {
            logger.error("Encode error : " + e.getMessage());
        } finally {
            lock.unlock();
        }
        System.out.println(Thread.currentThread().getName()
                + " : encode() longUrl : "
                + longUrl + " :  ----> shortUrl : " + shortUrl);
        return shortUrl;
    }

    /**
     * Decodes a shortened URL to its original URL
     *
     * @param shortUrl
     * @return
     */
    public String decode(String shortUrl) {
        if (shortUrl == null
                || shortUrl.length() <= 0
                //|| !shortUrl.startsWith(BASE_HOST)
        ) {
            return shortUrl;
        }
        System.out.println(Thread.currentThread().getName()
                + " : decode() shortUrl : " + shortUrl + " %%%%%%%%%%->  longUrl : "
                + shortUrlMapping.get(shortUrl));
        return shortUrlMapping.get(shortUrl);
    }

    private String generateRandomShortUrl() {
        StringBuilder sb = new StringBuilder();
        lock.lock();
        try {
            for (int i = 0; i < K; i++) {
                int randomIndex = ThreadLocalRandom.current().nextInt(0, SIZE62);
                sb.append(BASE62.charAt(randomIndex));
            }
        } catch (Exception e) {
            logger.error("generateRandomShortUrl Error : " + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        } finally {
            lock.unlock();
        }
        return sb.toString();
    }

    /*@Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            shortUrl = generateRandomShortUrl();
            System.out.println(Thread.currentThread().getName() + " : shortUrl : " + shortUrl);
        }
    }*/

}
