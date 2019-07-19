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

    private HttpStatus status;
    /** 异常状态码 */
    private String error_code;
    /** 异常信息 */
    private String message;
    /** 异常描述 */
    private String detail;

}
