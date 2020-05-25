package com.demo.test.exception;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 自定义的异常显示类
 *
 * @author Jack
 * @date 2019/07/17
 */
@Data
@NoArgsConstructor
@Builder
public class ApiErrorResponse extends RuntimeException {
    /**
     * Http返回状态
     */
    private HttpStatus status;
    /**
     * Http返回状态码
     */
    private String code;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回描述
     */
    private String detail;

    public ApiErrorResponse(HttpStatus status, String code, String message, String detail) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.detail = detail;
    }

    public ApiErrorResponse(String message, HttpStatus status, String code, String message1, String detail) {
        super(message);
        this.status = status;
        this.code = code;
        this.message = message1;
        this.detail = detail;
    }

    public ApiErrorResponse(String message, Throwable cause, HttpStatus status, String code, String message1, String detail) {
        super(message, cause);
        this.status = status;
        this.code = code;
        this.message = message1;
        this.detail = detail;
    }

    public ApiErrorResponse(Throwable cause, HttpStatus status, String code, String message, String detail) {
        super(cause);
        this.status = status;
        this.code = code;
        this.message = message;
        this.detail = detail;
    }

    public ApiErrorResponse(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, HttpStatus status, String code, String message1, String detail) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.status = status;
        this.code = code;
        this.message = message1;
        this.detail = detail;
    }
}
