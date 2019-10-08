package com.demo.test.utils;

import com.demo.test.constant.Constant;
import com.demo.test.domain.TokenModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 通过Redis存储和验证token的实现类
 *
 * @author Jack
 * @date 2019/05/30 14:36 PM
 * @date 2019/10/08 19:09 PM update
 */
@Component
public class TokenUtils {

    /**
     * 加盐
     */
    public static final String SALT = "sadmlf1$789787aadfjkds'[]jfeu;384785*^*&%^%$%";
    static Logger logger = LogManager.getLogger(TokenUtils.class);
    static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static Map<Long, TokenModel> tokenMap = new HashMap<>();
    private static TokenUtils tokenUtils = null;

    /*private RedisTemplate redis;

    @Autowired
    public void setRedis(RedisTemplate redis) {
        this.redis = redis;
        //泛型设置成Long后必须更改对应的序列化方案
        redis.setKeySerializer(new JdkSerializationRedisSerializer());
    }*/

    static {
        listenTask();
        synInit();
    }

    private TokenUtils() {
    }

    private static void synInit() {
        if (tokenUtils == null) {
            tokenUtils = new TokenUtils();
        }
    }

    public static Map<Long, TokenModel> getTokenMap() {
        return tokenMap;
    }

    /**
     * 生成一个token
     *
     * @param name
     * @param id
     * @return
     */
    public static TokenModel generateToken(String name, long id) {
        String signature = MD5(System.currentTimeMillis() + SALT + name + id);
        TokenModel token = new TokenModel(id, signature, System.currentTimeMillis());
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
        TokenModel token = tokenMap.get(id);
        if (token != null && token.getSignature().equals(signature)) {
            logger.info(token.getId() + "=======已在线=======sign : " + token.getSignature());
            return true;
        }
        return false;
    }

    public static boolean volidateToken(String signature) {
        if (signature == null || signature.length() <= 0) {
            throw new IllegalArgumentException("token can not be null");
        }
        for (TokenModel value : tokenMap.values()) {
            if (signature.equals(value.getSignature())) {
                return true;
            }
        }
        return false;
    }

    public static boolean volidateToken(TokenModel model) {
        if (model == null) {
            return false;
        }
        //String token = redis.boundValueOps(model.getUserId()).get();
        TokenModel tokenObject = tokenMap.get(model.getId());
        String token = tokenObject.getSignature();
        LoggerUtils.logInfo(logger, "checkToken : " + token);
        //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        return token != null && token.equals(model.getToken());
        //redis.boundValueOps(model.getUserId()).expire(Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
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
            byte[] btInput = s.getBytes(StandardCharsets.UTF_8);
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
        calendar.set(year, month, day + 1, Constant.HOUR, 0, 0);
        // calendar.set(year, month, day, 17, 11, 40);
        Date date = calendar.getTime();
        scheduler.scheduleAtFixedRate(new ListenToken(), (date.getTime() -
                System.currentTimeMillis()) / 1000, 60 * 60 * 24, TimeUnit.SECONDS);
    }

    public static String createToken(long userId) {
        //使用uuid作为源token
        String token = UUID.randomUUID().toString().replace("-", "");
        //TokenModel model = new TokenModel(userId, token);
        //存储到redis并设置过期时间
        //redis.boundValueOps(userId).set(token, Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        TokenModel tokenObject = new TokenModel();
        tokenObject.setSignature(token);
        tokenObject.setTimeStamp(System.currentTimeMillis());
        tokenMap.put(userId, tokenObject);
        return token;
    }

    public static TokenModel getToken(String authentication) {
        if (authentication == null || authentication.length() == 0) {
            return null;
        }
        String[] param = authentication.split("_");
        if (param.length != 2) {
            return null;
        }
        //使用userId和源token简单拼接成的token，可以增加加密措施
        long userId = Long.parseLong(param[0]);
        String token = param[1];
        return new TokenModel(userId, token);
    }

    public static void deleteToken(long userId) {
        //redis.delete(userId);
        tokenMap.remove(userId);
        LoggerUtils.logInfo(logger, "deleteToken =====> userId : " + userId);
    }

    public static boolean hasToken(String token) {
        return token != null && token.trim().length() > 0;
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
            logger.info("\n*****************执行监听token列表*****************");
            try {
                synchronized (tokenMap) {
                    for (int i = 0; i < 5; i++) {
                        if (tokenMap != null && !tokenMap.isEmpty()) {
                            for (Map.Entry<Long, TokenModel> entry : tokenMap.entrySet()) {
                                TokenModel token = entry.getValue();
                                logger.info("\n==============已登录用户有：" + entry + "==================");
                                //try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
                                int interval =
                                        (int) ((System.currentTimeMillis() - token.getTimeStamp())
                                                / Constant.ONE_THOUSAND
                                                / Constant.MINITES_IN_ONE_HOUR
                                                / Constant.SECONDS_IN_ONE_MINITES
                                                / Constant.HOURS_IN_ONE_DAY);
                                if (interval > Constant.INTERVAL) {
                                    tokenMap.remove(entry.getKey());
                                    logger.info("\n==============移除token：" + entry + "==================");
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
