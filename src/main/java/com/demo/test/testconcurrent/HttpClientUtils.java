package com.demo.test.testconcurrent;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 通用HttpClientUtil工具类
 */
@Slf4j
public class HttpClientUtils {

    public static String doGet(String url, Map<String, String> param) {
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            // 执行请求
            try (CloseableHttpClient httpclient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpclient.execute(httpGet)) {
                if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                    return EntityUtils.toString(response.getEntity(), "UTF-8");
                }
            }
        } catch (Exception e) {
            log.error("doGet method has exception : {} " + e.getMessage());
        }

        return "";
    }

    public static String doGet(String url) {
        return doGet(url, null);
    }

    public static String doPost(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建Http Post请求
        HttpPost httpPost = new HttpPost(url);
        // 创建参数列表
        if (param != null) {
            List<NameValuePair> paramList = new ArrayList<>();
            for (String key : param.keySet()) {
                paramList.add(new BasicNameValuePair(key, param.get(key)));
            }
            // 模拟表单
            UrlEncodedFormEntity entity = null;
            try {
                entity = new UrlEncodedFormEntity(paramList, "utf-8");
            } catch (UnsupportedEncodingException e) {
                log.error("UrlEncodedFormEntity has exception : {} " + e.getMessage());
            }
            httpPost.setEntity(entity);
        }
        // 执行http请求
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            return EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            log.error("httpClient.execute(httpPost) has exception : {} " + e.getMessage());
        }

        return "";
    }

    public static String doPost(String url) {
        return doPost(url, null);
    }

    public static String doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建Http Post请求
        HttpPost httpPost = new HttpPost(url);
        // 创建请求内容
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        // 执行http请求
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            return EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            log.error("doPostJson method has exception : {} " + e.getMessage());
        }

        return "";
    }

    public static Object doPost2(String url, Map<String, Object> headers, String body) {
        Assert.notEmpty(Collections.singleton(url), "Url can not be null!");
        Assert.notEmpty(headers, "Input parameters can not be null!");
        HttpPost httpPost = new HttpPost(url);
        try {
            //设置请求头
            for (String key : headers.keySet()) {
                httpPost.setHeader(key, headers.get(key).toString());
            }
            //设置请求体
            httpPost.setEntity(new StringEntity(body));
        } catch (Exception e) {
            log.error("doPost2 method has exception : {} " + e.getMessage());
        }
        //创建httpclient实例
        try (CloseableHttpClient httpclient = HttpClients.createDefault();
             CloseableHttpResponse response = httpclient.execute(httpPost)) {
            int status = response.getStatusLine().getStatusCode();
            //响应体字符串
            HttpEntity httpEntity = response.getEntity();
            String responesBody = EntityUtils.toString(httpEntity, "UTF-8");
            try {
                return JSONObject.parseObject(responesBody, Feature.OrderedField);
            } catch (Exception e) {
                log.error("parseObject has exception : {} " + e.getMessage());
            }
        } catch (Exception e) {
            log.error("httpclient.execute(httpPost) has exception : {} " + e.getMessage());
        }

        return "";
    }

}
