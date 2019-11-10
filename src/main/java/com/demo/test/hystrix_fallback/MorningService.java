package com.demo.test.hystrix_fallback;

public class MorningService {

    /**
     * 超时
     */
    public void timeout() {
        int j = 0;
        while (true) {
            j++;
        }
    }

}