package com.demo.test.getway;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 *  优化后API的写法，并行执行
 *  优化用MyFutureTask去代替FutureTask
 *
 *   @author Jack
 */

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
                System.out.println(Thread.currentThread() + " 调用第一个新的 API 时间是 ： " + (System.currentTimeMillis() - userinfoTime));
                return userInfo;
            }
        };

        Callable<JSONObject> weatherInfoCallable = new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws Exception {
                String url = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=%E5%A4%A9%E6%B4%A5%E5%A4%A9%E6%B0%94";
                long userinfoTime = System.currentTimeMillis();
                String value = restTemplate.getForObject(url, String.class);
                JSONObject weatherInfo = JSONObject.parseObject(value);
                System.out.println(Thread.currentThread() + "调用第二个新的 API 时间是 ： " + (System.currentTimeMillis() - userinfoTime));
                return weatherInfo;
            }
        };

        //FutureTask<JSONObject> userInfo = new FutureTask<>(userInfoCallable);
        //FutureTask<JSONObject> weatherInfo = new FutureTask<>(weatherInfoCallable);
        MyFutureTask<JSONObject> userInfo = new MyFutureTask<>(userInfoCallable);
        MyFutureTask<JSONObject> weatherInfo = new MyFutureTask<>(weatherInfoCallable);
        new Thread(userInfo).start();
        new Thread(weatherInfo).start();

        JSONObject result = new JSONObject();
        result.putAll(userInfo.get());
        result.putAll(weatherInfo.get());
        return result;
    }

}
