package com.demo.test.testing;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public class HttpClientTest {

    public static void main(String[] args) {
        //请求路径
        String url = "http://localhost:8081/admin/query2";
        //请求头
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        //请求资源
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("username", "xiaoming");
        bodyMap.put("password", "123");
        String body = JSON.toJSONString(bodyMap);

        Object response = HttpClientUtils.doPost2(url, headers, body);
        System.out.println(response.toString());

    }

}
