package com.demo.test.getway;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *  最初API的写法，串行执行
 *
 *  @author Jack
 */

@Component
public class UserServiceOld {

    /** 在启动类里实例化 */
    @Autowired
    RestTemplate restTemplate;

    public Object getUserInfo(String userName) throws Exception {

        String url = "http://t.weather.sojson.com/api/weather/city/101030100";
        long userinfoTime = System.currentTimeMillis();
        String value = restTemplate.getForObject(url, String.class);
        JSONObject userInfo = JSONObject.parseObject(value);
        System.out.println(Thread.currentThread() + "调用第一个旧的 API 的调用时间是 ： " + (System.currentTimeMillis() - userinfoTime));

        String url2 = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=%E5%A4%A9%E6%B4%A5%E5%A4%A9%E6%B0%94";
        long userinfoTime2 = System.currentTimeMillis();
        String value2 = restTemplate.getForObject(url2, String.class);
        JSONObject weatherInfo = JSONObject.parseObject(value2);
        System.out.println(Thread.currentThread() + "调用第二个旧的 API 的调用时间是 ： " + (System.currentTimeMillis() - userinfoTime2));

        JSONObject result = new JSONObject();
        result.putAll(userInfo);
        result.putAll(weatherInfo);
        return result;
    }

}
