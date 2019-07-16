package com.demo.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Spring Boot REST 统一异常处理
 *
 * @author Jack
 * @date 2019/07/17
 */
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(CustomNotFoundException.class)
    public ApiErrorResponse handleNotFoundException(CustomNotFoundException ex) {

        ApiErrorResponse response =new ApiErrorResponse.ApiErrorResponseBuilder()
                .withStatus(HttpStatus.NOT_FOUND)
                .withError_code("NOT_FOUND")
                .withMessage(ex.getLocalizedMessage()).build();

        return response;
    }
}
