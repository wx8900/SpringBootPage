package com.demo.test.utils;

import com.demo.test.domain.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TokenUtil {

    private static final int INTERVAL = 7;// token过期时间间隔七天
    private static final String SALT = "sadmlf1$789787aadfjkds'[]jfeu;384785*^*&%^%$%";// 加盐
    private static final int HOUR = 3;// 检查token过期线程执行时间 时
    static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    static Logger logger = LogManager.getLogger(TokenUtil.class);
    private static Map<Long, Token> tokenMap = new HashMap<>();
    private static TokenUtil tokenUtil = null;

    static {
        //开启监听
        listenTask();
    }

    private TokenUtil() {
    }

    public static TokenUtil getTokenUtil() {
        if (tokenUtil == null) {
            synInit();
        }
        return tokenUtil;
    }

    private static synchronized void synInit() {
        if (tokenUtil == null) {
            tokenUtil = new TokenUtil();
        }
    }

    public static Map<Long, Token> getTokenMap() {
        return tokenMap;
    }

    /**
     * 生成一个token
     *
     * @param name
     * @param id
     * @return
     */
    public static Token generateToken(String name, long id) {
        String signature = MD5(System.currentTimeMillis() + SALT + name + id);
        Token token = new Token(signature, System.currentTimeMillis(), id);
        synchronized (tokenMap) {
            tokenMap.put(id, token);
        }
        return token;
    }

    /**
     * checkTime方法
     *
     * @param time
     * @param variable
     * @return
     */
    public static boolean checkTime(Long time, Integer variable) {
        Long currentTimeMillis = System.currentTimeMillis();
        Long addTime = currentTimeMillis + variable;
        Long subTime = currentTimeMillis - variable;
        return addTime > time && time > subTime;
    }

    /**
     * 校验token 2
     *
     * @param signature
     * @param id
     * @return
     */
    public static boolean volidateToken(String signature, long id) {
        if (signature == null || signature.length() <= 0) {
            throw new IllegalArgumentException("signature can not be null");
        }
        /*long nowTime = System.currentTimeMillis();
        // 接口请求的时间在600秒钟之前的直接抛弃
        if ((nowTime - timestamp) > 600000) {
            return false;
        }*/
        Token token = tokenMap.get(id);
        if (token != null && token.getSignature().equals(signature)) {
            logger.info("=======已在线=======");
            return true;
        }
        return false;
    }

    public static boolean volidateToken(String signature) {
        if (signature == null || signature.length() <= 0) {
            throw new IllegalArgumentException("token can not be null");
        }
        for (Token value : tokenMap.values()) {
            if (signature.equals(value.getSignature())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 去除token
     *
     * @param id
     * @return boolean
     */
    public static boolean removeToken(long id) {
        synchronized (tokenMap) {
            tokenMap.remove(id);
            logger.info(tokenMap.get(id) == null ? "\n=========已注销========" : "\n++++++++注销失败++++++++");
        }
        return true;
    }

    /**
     * MD5加密
     *
     * @param s
     * @return String
     */
    public final static String MD5(String s) {
        try {
            byte[] btInput = s.getBytes("UTF-8");
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            return byte2hex(mdInst.digest());
        } catch (Exception e) {
            logger.error("MD5 Method has exception!", e);
            return null;
        }
    }

    /**
     * 将字节数组转换成16进制字符串
     *
     * @param b
     * @return
     */
    private static String byte2hex(byte[] b) {
        StringBuilder sbDes = new StringBuilder();
        String tmp;
        for (int i = 0, size = b.length; i < size; i++) {
            tmp = (Integer.toHexString(b[i] & 0xFF));
            if (1 == tmp.length()) {
                sbDes.append("0");
            }
            sbDes.append(tmp);
        }
        return sbDes.toString();
    }

    /**
     * 定时执行token过期清除任务
     */
    public static void listenTask() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //定制每天的HOUR点，从明天开始
        calendar.set(year, month, day + 1, HOUR, 0, 0);
        // calendar.set(year, month, day, 17, 11, 40);
        Date date = calendar.getTime();
        scheduler.scheduleAtFixedRate(new ListenToken(), (date.getTime() -
                System.currentTimeMillis()) / 1000, 60 * 60 * 24, TimeUnit.SECONDS);
    }

    /**
     * 监听token过期线程runnable实现
     */
    static class ListenToken implements Runnable {
        public ListenToken() {
            super();
        }

        public static void main(String[] args) {
            System.out.println(generateToken("s", 1));
            System.out.println(generateToken("q", 1));
            System.out.println(generateToken("s3", 2));
            System.out.println(generateToken("s4", 3));
            System.out.println(removeToken(3));
            System.out.println(getTokenMap());
        }

        @Override
        public void run() {
            logger.info("\n**************************执行监听token列表****************************");
            try {
                synchronized (tokenMap) {
                    for (int i = 0; i < 5; i++) {
                        if (tokenMap != null && !tokenMap.isEmpty()) {
                            for (Entry<Long, Token> entry : tokenMap.entrySet()) {
                                Token token = entry.getValue();
                                logger.info("\n==============已登录用户有：" + entry + "=====================");
//                            try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
                                int interval = (int) ((System.currentTimeMillis() - token.getTimestamp()) / 1000 / 60 / 60 / 24);
                                if (interval > INTERVAL) {
                                    tokenMap.remove(entry.getKey());
                                    logger.info("\n==============移除token：" + entry + "=====================");
                                }

                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("token监听线程错误：" + e.getMessage().substring(0, 500));
            }
        }
    }
}
