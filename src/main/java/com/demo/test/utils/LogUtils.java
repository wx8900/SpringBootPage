package com.demo.test.utils;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.*;

/**
 *
 * 用于记录日志入库等方法
 *
 * @author  Jack
 * @date    2019/06/01  23:01 PM
 *
 */
public class LogUtils {
    private static Map<String,Logger> loggerMap = new HashMap<>();

    public static void debug(Object message){
        String className = getClassName();
        Logger log = getLogger(className);
        if(log.isDebugEnabled()){
            log.debug(message);
        }
    }

    public static void debug(String tag, Object message){
        String className = getClassName();
        Logger log = getLogger(className);
        if(log.isDebugEnabled()){
            log.debug(new StringBuffer().append("【").append(tag).append("】").append(message).toString());
        }
    }

    public static void info(Object message){
        String className = getClassName();
        Logger log = getLogger(className);
        if(log.isInfoEnabled()){
            log.info(message);
        }
    }

    public static void info(String tag, Object message){
        String className = getClassName();
        Logger log = getLogger(className);
        if(log.isInfoEnabled()){
            log.info(new StringBuffer().append("【").append(tag).append("】").append(message).toString());
        }
    }

    public static void warn(Object message){
        String className = getClassName();
        Logger log = getLogger(className);
        log.warn(message);
    }

    public static void warn(String tag, Object message){
        String className = getClassName();
        Logger log = getLogger(className);
        log.warn(new StringBuffer().append("【").append(tag).append("】").append(message).toString());
    }

    public static void error(Object message){
        String className = getClassName();
        Logger log = getLogger(className);
        log.error(message);
    }

    public static void error(String tag, Object message){
        String className = getClassName();
        Logger log = getLogger(className);
        log.error(new StringBuffer().append("【").append(tag).append("】").append(message).toString());
    }

    /**
     * @author phubing
     *
     * 获取最开始的调用者所在类
     */
    private static String getClassName(){
        Throwable th = new Throwable();
        StackTraceElement[] stes = th.getStackTrace();
        StackTraceElement ste = stes[2];
        return ste.getClassName();
    }
    /**
     * @author phubing
     *
     * 根据类名获得logger对象
     */
    private static Logger getLogger(String className){
        Logger log = null;
        if(loggerMap.containsKey(className)){
            log = loggerMap.get(className);
        }else{
            try {
                log = Logger.getLogger(Class.forName(className));
                loggerMap.put(className, log);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return log;
    }


    public void insertLog() {

    }

}
