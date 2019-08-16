package com.demo.test.utils;

import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Special character detection tool to prevent from
 * illegal characters input and SQL injection attacks
 *
 * @author Jack
 * @date 2019/07/16
 */
public class IllegalStrFilterUtils {

    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(IllegalStrFilterUtils.class);

    private static final String REGX = "!|！|@|◎|#|＃|(\\$)|￥|%|％|(\\^)|……|(&)|※|(\\*)|×|(\\()|（|(\\))|）|_|——|(\\+)|＋|(\\|)|§ ";

    /**
     * Intercepting common SQL injection attacks
     *
     * @param sInput
     * @return true only if no risk for SQL injection
     * false
     */
    public static Boolean sqlStrFilter(String sInput) {
        if (sInput == null || sInput.trim().length() == 0) {
            return false;
        }
        sInput = sInput.toUpperCase();

        if (sInput.indexOf("DELETE") >= 0 || sInput.indexOf("ASCII") >= 0 || sInput.indexOf("UPDATE") >= 0
                || sInput.indexOf("SELECT") >= 0 || sInput.indexOf("'") >= 0 || sInput.indexOf("SUBSTR(") >= 0
                || sInput.indexOf("COUNT(") >= 0 || sInput.indexOf(" OR ") >= 0 || sInput.indexOf(" AND ") >= 0
                || sInput.indexOf("DROP") >= 0 || sInput.indexOf("EXECUTE") >= 0 || sInput.indexOf("EXEC") >= 0
                || sInput.indexOf("TRUNCATE") >= 0 || sInput.indexOf("INTO") >= 0 || sInput.indexOf("DECLARE") >= 0
                || sInput.indexOf("MASTER") >= 0) {
            Logger.error("input String has SQL injection risk: sInput=" + sInput);
            return false;
        }
        Logger.info("Successfully passed the intercept");
        return true;
    }

    /**
     * Detect illegal characters
     *
     * @param sInput
     * @return true only if the input String doesn't contains illegal characters
     *
     */
    public static Boolean isIllegalStr(String sInput) {

        if (sInput == null || sInput.trim().length() == 0) {
            return false;
        }
        sInput = sInput.trim();
        Pattern compile = Pattern.compile(REGX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compile.matcher(sInput);
        Logger.info("通过字符串检测");
        return matcher.find();
    }

}
