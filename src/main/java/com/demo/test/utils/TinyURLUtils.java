package com.demo.test.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Jack
 * @date 2019/06/22 01:24 AM
 */
public class TinyURLUtils implements Runnable {

    static final int K = 6;
    private static final String BASE_HOST = "http://tinyurl.com/";
    private static final AtomicLong atomicLong = new AtomicLong();
    private static final ConcurrentHashMap<String, String> encodedToUrl = new ConcurrentHashMap<>();

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
        Thread ta = new Thread(r, "Thread-A");
        Thread tb = new Thread(r, "Thread-B");
        Thread tc = new Thread(r, "Thread-C");
        ta.start();
        tb.start();
        tc.start();
    }

    /**
     * Encodes a URL to a shortened URL
     *
     * @param longUrl
     * @return
     */
    public synchronized String encode(String longUrl) {
        if (longUrl == null || longUrl.length() <= 0) {
            return longUrl;
        }
        //String uniqueURL = String.valueOf(atomicLong.getAndIncrement());
        //encodedToUrl.put(uniqueURL, longUrl);
        String shortUrl = generateRandomShortUrl();
        while (encodedToUrl.containsKey(shortUrl)) {
            shortUrl = generateRandomShortUrl();
        }
        encodedToUrl.put(shortUrl, longUrl);
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

    private synchronized String generateRandomShortUrl() {
        final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final int size = BASE62.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < K; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(0, size);
            sb.append(BASE62.charAt(randomIndex));
        }
        return sb.toString();
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            String shortUrl = "";
            try {
                shortUrl = generateRandomShortUrl();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " : shortUrl : " + shortUrl);
        }
    }

}
