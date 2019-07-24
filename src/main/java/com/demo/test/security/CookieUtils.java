package com.demo.test.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jack
 * @date 2019/07/24 03:11 AM
 */
public class CookieUtils {

    // Token保存在这个cookie名下
    public static final String COOKIE_NAME_SESSION = "Jssionid";

    public static String getRequestedToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie == null) {
                continue;
            }

            if (!COOKIE_NAME_SESSION.equalsIgnoreCase(cookie.getName())) {
                continue;
            }
            return cookie.getValue();
        }
        return "";
    }

    /**
     * 种Cookie
     *
     * @param token
     * @param response
     */
    public static void flushCookie(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME_SESSION, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        //Cookie只有在具体的域名下，才有用
        cookie.setDomain("dev.com");
        cookie.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(cookie);
    }


}
