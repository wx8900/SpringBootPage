package com.demo.test.constant;

/**
 * @author Jack
 * @date 2019/10/08 19:27 PM
 */
public class Constant {
    public static final String NO_TOKEN = "No token found!";
    public static final String TOKEN_INVALID = "This token is unauthorized, authorization failure";
    public static final String NO_LOGIN_USER = "Current user is null!";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";
    public static final String OPTIONS = "OPTIONS";

    public static final String BASE_PACKAGE = "com.demo.test";

    /**  token过期时间间隔七天 */
    public static final int INTERVAL = 7;
    /**  检查token过期线程执行时间 */
    public static final int HOUR = 3;
    public static final int SECONDS_30 = 30;
    public static final int ONE_THOUSAND = 1000;
    public static final int HOURS_IN_ONE_DAY = 24;
    public static final int MINITES_IN_ONE_HOUR = 60;
    public static final int SECONDS_IN_ONE_MINITES = 60;

    public static final String KEY = "cacheKey";


    /** 安全 */
    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";
    public static final String RSA_ALGORITHM_SIGN = "SHA256WithRSA";

}
