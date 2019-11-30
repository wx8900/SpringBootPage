package com.demo.test.exception;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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

}
