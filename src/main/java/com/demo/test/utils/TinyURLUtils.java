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
 */
public class TinyURLUtils implements Runnable {

    private static final int K = 6;
    private static final String BASE_HOST = "http://tinyurl.com/";
    /**
     * private static final AtomicLong atomicLong = new AtomicLong();
     */
    private static final ConcurrentHashMap<String, String> encodedToUrl = new ConcurrentHashMap<>();
    static Logger logger = LogManager.getLogger("TinyURLUtils");
    private final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final int SIZE62 = BASE62.length();
    String shortUrl = "";
    Lock lock = new ReentrantLock();
    //Lock lock = new MyLock();

    public static void main(String[] args) {
        TinyURLUtils tinyURLUtils = new TinyURLUtils();
        String encodeString = tinyURLUtils.encode("https://www.youtube.com/watch?v=8aG3Z62rfghk");
        System.out.println("encode is : " + encodeString);
        System.out.println("decode is : " + tinyURLUtils.decode(encodeString));

        String encodeString2 = tinyURLUtils.encode("https://www.youtube.com/watch?v=8aG3Z62rfdsa");
        System.out.println("encode2 is : " + encodeString2);
        System.out.println("decode2 is : " + tinyURLUtils.decode(encodeString2));

        String encodeString3 = tinyURLUtils.encode("https://www.youtube.com/watch?v=8aG3Z611111");
        System.out.println("encode3 is : " + encodeString3);
        System.out.println("decode3 is : " + tinyURLUtils.decode(encodeString3));

        TinyURLUtils r = new TinyURLUtils();
        // 3个线程同时跑
        for (int i = 0; i < 3; i++) {
            new Thread(r, "Thread-" + i).start();
        }
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
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
        try {
            if ("".equals(shortUrl)) {
                shortUrl = generateRandomShortUrl();
            }
            while (encodedToUrl.containsKey(shortUrl)) {
                shortUrl = generateRandomShortUrl();
            }
            encodedToUrl.put(shortUrl, longUrl);
        } catch (Exception e) {
            logger.error("encode Error : " + e.getMessage());
        } finally {
            lock.unlock();
        }

        return BASE_HOST + shortUrl;
    }

    /**
     * Decodes a shortened URL to its original URL
     *
     * @param shortUrl
     * @return
     */
    public String decode(String shortUrl) {
        if (shortUrl == null || shortUrl.length() <= 0 || !shortUrl.startsWith(BASE_HOST)) {
            return shortUrl;
        }
        return encodedToUrl.get(shortUrl.substring(BASE_HOST.length()));
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

    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                shortUrl = generateRandomShortUrl();
                System.out.println(Thread.currentThread().getName() + " : shortUrl : " + shortUrl);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}
