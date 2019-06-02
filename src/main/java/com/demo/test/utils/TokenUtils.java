package com.demo.test.utils;

import com.demo.test.domain.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 通过Redis存储和验证token的实现类
 *
 * @author Jack
 * @date 2019/05/30 14:36 PM
 */
@Component
public class TokenUtils {

    private static final Logger logger = LoggerFactory.getLogger(TokenUtils.class);

    private static Map<Long, String> tokenMap = new HashMap<>();

    /*private RedisTemplate redis;

    @Autowired
    public void setRedis(RedisTemplate redis) {
        this.redis = redis;
        //泛型设置成Long后必须更改对应的序列化方案
        redis.setKeySerializer(new JdkSerializationRedisSerializer());
    }*/

    public static String createToken(long userId) {
        //使用uuid作为源token
        String token = UUID.randomUUID().toString().replace("-", "");
        //TokenModel model = new TokenModel(userId, token);
        //存储到redis并设置过期时间
        //redis.boundValueOps(userId).set(token, Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        tokenMap.put(userId, token);
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

    public static boolean checkToken(TokenModel model) {
        if (model == null) {
            return false;
        }
        //String token = redis.boundValueOps(model.getUserId()).get();
        String token = tokenMap.get(model.getId());
        LoggerUtils.logInfo(logger, "checkToken : " + token);
        return token != null && token.equals(model.getToken());//如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        //redis.boundValueOps(model.getUserId()).expire(Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
    }

    public static void deleteToken(long userId) {
        //redis.delete(userId);
        tokenMap.remove(userId);
        LoggerUtils.logInfo(logger, "deleteToken =====> userId : " + userId);
    }
}
