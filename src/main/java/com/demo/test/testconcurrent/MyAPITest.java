package com.demo.test.testconcurrent;

import java.util.HashMap;
import java.util.Map;

/**
 * Test Post API, Test OK
 * return Json Data
 *
 * @author Jack
 * @date 2019/10/08 19:46 PM
 */
public class MyAPITest {

    public static void main(String[] args) {
        //String url = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?";
        //String url = "http://cache.video.iqiyi.com/jp/avlist/202861101/1/?callback=jsonp9";
        String url = "https://www.apiopen.top/journalismApi";
        // 参数要放入map
        //String url = "http://www.tudou.com/tvp/getMultiTvcCodeByAreaCode.action?type=3&app=4&codes=Lqfme5hSolM&areaCode=320500&jsoncallback=__TVP_getMultiTvcCodeByAreaCode";
        Map<String, String> map = new HashMap<>();
        //map.put("tel", "15921371232");
        String result = HttpClientUtils.doPost(url, map);
        System.out.println(result);
    }
}
