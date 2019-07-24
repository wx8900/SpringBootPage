package com.demo.test.security;

import com.demo.test.utils.LoggerUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Util class for operate Cookie
 *
 * @author Jack
 * @date 2019/07/24 03:11 AM
 */
public class CookieUtils {

    /**
     * Save Token under this cookie name
     */
    public static final String COOKIE_NAME_SESSION = "Jssionid";

    static Logger logger = LogManager.getLogger(CookieUtils.class);

    private static int iDefaultValidSecond = -1;

    private CookieUtils() {
    }

    /**
     * Get token from request
     *
     * @param request
     * @return
     */
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
     * Save Cookie
     *
     * @param token
     * @param response
     */
    public static void flushCookie(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME_SESSION, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        // Cookie is valid only under a specific domain name
        cookie.setDomain("dev.com");
        cookie.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(cookie);
    }

    /**
     * Put cookie to the client
     *
     * @param response output
     * @param name     Cookie's name
     * @param value    Cookie's value
     */
    public static void put(HttpServletResponse response, String name, String value) {
        try {
            Cookie cookie = new Cookie(name, encode(value));
            cookie.setPath("/");
            // cookie.setDomain("");
            cookie.setMaxAge(iDefaultValidSecond);
            response.addCookie(cookie);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    /**
     * save value into cookie，invalid after close the browser
     *
     * @param request  HttpServletRequest
     * @param response output
     * @param name     Cookie's name
     * @param value    Cookie's value
     */
    public static void put(HttpServletRequest request, HttpServletResponse response, String name, String value) {
        put(request, response, name, value, iDefaultValidSecond);
    }

    /**
     * set a Cookie, availabel for set up live time, unit is second
     *
     * @param request
     * @param response
     * @param name         Cookie's name
     * @param value        Cookie's value
     * @param iValidSecond Cookie's live time
     */
    public static void put(HttpServletRequest request, HttpServletResponse response, String name, String value, int iValidSecond) {
        try {
            Cookie cookie = new Cookie(name, encode(value));
            setCookieProperty(request, cookie);
            cookie.setMaxAge(iValidSecond);
            response.addCookie(cookie);
        } catch (Exception ex) {
            LoggerUtils.logError(logger, ex.getMessage());
        }
    }

    /**
     * get the name of server
     *
     * @param request HttpServletRequest
     * @return String server domain name
     */
    public static String getDomain(HttpServletRequest request) {
        String serverName = request.getServerName();
        int index = serverName.indexOf(".");
        if (index > 0) {
            serverName = StringUtils.substring(serverName, index);
        }
        return serverName;
    }

    /**
     * Cookie path and domain name
     *
     * @param request HttpServletRequest
     * @param cookie  Cookie
     */
    public static void setCookieProperty(HttpServletRequest request, Cookie cookie) {
        if (null != cookie) {
            // share Cookie with second and multi sub-domain name
            cookie.setDomain(getDomain(request));
            cookie.setPath("/");
        }
    }

    /**
     * get cookie from client
     *
     * @param request HttpServletRequest
     * @param name    the cookie name of getting value wanted
     * @return String cookie's value
     */
    public static String get(HttpServletRequest request, String name) {
        Cookie[] cookies = readAll(request);
        if (cookies == null) {
            return "";
        }
        String result = "";
        try {
            for (Cookie cookie : cookies) {
                setCookieProperty(request, cookie);
                if (cookie.getName().equals(name)) {
                    result = cookie.getValue();
                    break;
                }
            }
        } catch (Exception ex) {
            LoggerUtils.logError(logger, ex.getMessage());
        }
        return decode(result);
    }

    /**
     * 清除Cookie
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param name     String
     */
    public static void remove(HttpServletRequest request, HttpServletResponse response, String name) {
        put(request, response, name, null, 0);
    }

    /**
     * delete all Cookie
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    public static void removeAll(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = readAll(request);
        if (cookies != null) {
            try {
                for (Cookie ck : cookies) {
                    Cookie cookie = new Cookie(ck.getName(), null);
                    setCookieProperty(request, cookie);
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            } catch (Exception ex) {
                LoggerUtils.logError(logger, ex.getMessage());
            }
        }
    }

    /**
     * get all Cookies
     *
     * @param request HttpServletRequest
     * @return Cookie[] all Cookie
     */
    public static Cookie[] readAll(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return cookies;
    }

    /**
     * Encode URL for given String
     *
     * @param value String
     * @return String
     */
    private static String encode(String value) {
        String result = "";
        value = StringUtils.trim(value);
        if (StringUtils.isNotEmpty(value)) {
            try {
                result = URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                LoggerUtils.logError(logger, ex.getMessage());
            }
        }
        return result;
    }

    /**
     * Decode URL for given String
     *
     * @param value String
     * @return String
     */
    private static String decode(String value) {
        String result = "";
        value = StringUtils.trim(value);
        if (StringUtils.isNotEmpty(value)) {
            try {
                result = URLDecoder.decode(value, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                LoggerUtils.logError(logger, ex.getMessage());
            }
        }
        return result;
    }

    /**
     * Determine the specific String exists in cookie or not
     *
     * @param request
     * @param key     cookie name of the value you want to fetch
     * @return
     */
    public static boolean isExists(HttpServletRequest request, String key) {
        Cookie[] cookies = readAll(request);
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                setCookieProperty(request, cookie);
                if (cookie.getName().equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

}
