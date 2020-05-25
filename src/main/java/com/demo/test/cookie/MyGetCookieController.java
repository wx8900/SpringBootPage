package com.demo.test.cookie;

import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 操作Cookie，从HttpServletRequest取到Cookie
 * http://localhost:8080/v1/api/cookie/getCookies
 */

@Api("Cookie operation")
@RestController
@Validated
@RequestMapping("/v1/api/cookie")
public class MyGetCookieController {

    @RequestMapping(value = "/setCookies",method = RequestMethod.GET)
    public  String setCookies(HttpServletResponse response) {
        //HttpServerletRequest 装请求信息类
        //HttpServerletRespionse 装相应信息的类
        Cookie cookie = new Cookie("sessionId", "CookieTestInfo");
        response.addCookie(cookie);
        return "添加cookies信息成功";
    }

    //非注解方式获取cookie中对应的key值

    @RequestMapping(value = "/getCookies",method = RequestMethod.GET)
    public  String getCookies(HttpServletRequest request){
        //HttpServletRequest 装请求信息类
        //HttpServletRespionse 装相应信息的类
        //   Cookie cookie=new Cookie("sessionId","CookieTestInfo");
        Cookie[] cookies =  request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("sessionId")){
                    return cookie.getValue();
                }
            }
        }
        return  null;
    }

    public static void main(String[] args) {
        
    }

}
