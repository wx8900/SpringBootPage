package com.demo.test.hystrix_fallback;

import org.junit.Test;

import java.io.IOException;

public class HystrixFallbackTest
        //extends HystrixCommand<String>
        {

//    private final String name;
//
//    private MorningService morningService;
//
//    public HystrixFallbackTest(String name, MorningService morningService) {
//        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("FallbackGroup"))
//                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(10000)));  // 10秒超时哦
//        this.name = name;
//        this.morningService = morningService;
//    }
//
//    @Override
//    protected String run() throws Exception {
//        /**
//         * 超时触发fallback
//         */
//        morningService.timeout();
//        return name;
//    }
//
//    /**
//     * Hystrix的降级方法
//     * @return
//     */
//    @Override
//    protected String getFallback() {
//        return "fallback: " + name;
//    }
//
//    public static class UnitTest {
//
//        @Test
//        public void testFallback() throws IOException {
//            System.out.println(new HystrixFallbackTest("Morning", new MorningService()).execute());
//        }
//    }
}
