package com.demo.test.testconcurrent;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Jack
 * @date 2019/10/08
 */
@Slf4j
public class GetDataFromAPI {

    public static void main(String[] args) throws Exception {
        json();
    }

    public static void json() throws Exception {
        //参数url化
        String city = URLEncoder.encode("上海", "utf-8");

        //拼地址
        String apiUrl = String.format("https://www.sojson.com/open/api/weather/json.shtml?city=%s", city);
        //开始请求
        URL url = new URL(apiUrl);
        URLConnection open = url.openConnection();
        InputStream input = open.getInputStream();
        //这里转换为String，带上包名，怕你们引错包
        String result = org.apache.commons.io.IOUtils.toString(input, StandardCharsets.UTF_8);
        //输出
        log.info("result : {} " + result);
    }
}
