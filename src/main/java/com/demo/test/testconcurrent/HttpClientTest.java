package com.demo.test.testconcurrent;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
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
        log.info("response.toString() : "+response.toString());

    }

}
