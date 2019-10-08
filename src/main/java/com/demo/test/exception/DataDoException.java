package com.demo.test.exception;

/**
 * 操作数据或库出现异常
 *
 * @author Jack
 */
public class DataDoException extends RuntimeException {

    public DataDoException(String msg) {
        super(msg);
    }

}
