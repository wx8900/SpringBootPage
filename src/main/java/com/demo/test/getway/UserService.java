package com.demo.test.getway;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;


@Component
public class UserService {

    /** 在启动类里实例化 */
    @Autowired
    RestTemplate restTemplate;

    public Object getUserInfo(String userName) throws Exception {
        Callable<JSONObject> userInfoCallable = new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws Exception {
                String url = "http://t.weather.sojson.com/api/weather/city/101030100";
                long userinfoTime = System.currentTimeMillis();
                String value = restTemplate.getForObject(url, String.class);
                JSONObject userInfo = JSONObject.parseObject(value);
                System.out.println(Thread.currentThread() + "User Info API 调用时间是 ： " + (System.currentTimeMillis() - userinfoTime));
                return userInfo;
            }
        };

        FutureTask<JSONObject> userInfo = new FutureTask<>(userInfoCallable);
        new Thread(userInfo).start();

        JSONObject result = new JSONObject();
        result.putAll(userInfo.get());
        return result;
    }

}
