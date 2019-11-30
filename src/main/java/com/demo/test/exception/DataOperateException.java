package com.demo.test.exception;

/**
 * 操作数据或库出现异常
 *
 * @author Jack
 */
public class DataOperateException extends RuntimeException {

    public DataOperateException(String msg) {
        super(msg);
    }

}
