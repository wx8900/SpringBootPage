package com.demo.test.testing;

import java.net.URL;
import java.net.URLConnection;

/**
 * @author Jack
 * @date 2019/10/08
 */
public class ConnectionUtil {

    public static void connect() throws Exception {
        //final String urlStr = "http://localhost/ssmbase/test";
        //final String urlStr = "http://api.map.baidu.com/telematics/v3/weather?location=嘉兴&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ";
        //final String urlStr = "http://php.weather.sina.com.cn/iframe/index/w_cl.php?code=js&day=0&city=&dfc=1&charset=utf-8";
        //final String urlStr = "https://www.sojson.com/blog/305.html";
        //final String urlStr = "http://suggest.taobao.com/sug?code=utf-8&q=手机&callback=cb";
        //final String urlStr = "http://t.weather.sojson.com/api/weather/city/101030100";
        //final String urlStr = "https://market.aliyun.com/products/57126001/cmapi019423.html#sku=yuncode1342300000";
        //final String urlStr = "https://www.apiopen.top/journalismApi";
        //final String urlStr="https://blog.52itstyle.com/";
        final String urlStr="https://www.baidu.com/";
        URL url = new URL(urlStr);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.connect();
        //System.out.println(urlConnection.getInputStream().read());
        System.out.println(urlConnection.getInputStream().read());
    }
}