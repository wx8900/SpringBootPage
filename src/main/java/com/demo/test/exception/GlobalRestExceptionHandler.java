package com.demo.test.exception;

import com.google.common.net.HttpHeaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 统一异常处理
 *
 * @author Jack
 * @date 2019/07/17
 */
@ControllerAdvice
@ResponseBody
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

    static Logger logger = LogManager.getLogger(GlobalRestExceptionHandler.class);

    /**
     * 当方法参数不是预期类型时，抛出MethodArgumentTypeMismatchException异常
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        ApiErrorResponse response = new ApiErrorResponse.ApiErrorResponseBuilder()
                .withStatus(status)
                .withError_code(HttpStatus.BAD_REQUEST.name())
                .withMessage(ex.getLocalizedMessage()).build();

        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * 当API无法读取HTTP消息时，抛出HttpMessageNotReadable异常
     * BeanCreationException:
     * Caused by: java.lang.IllegalStateException: Ambiguous @ExceptionHandler method
     * mapped for [class org.springframework.web.bind.MethodArgumentNotValidException]
     * 只要重写父类方法即可。不要在重写的方法上声明拦截异常。
     * 对于ResponseEntityExceptionHandler已经定义好的异常，不需要重新定义，只要重写方法就可以了
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    //@ExceptionHandler({HttpMessageNotReadableException.class})
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request ";
        ApiErrorResponse response = new ApiErrorResponse.ApiErrorResponseBuilder()
                .withStatus(status)
                .withError_code("BAD_DATA")
                .withMessage(ex.getLocalizedMessage())
                .withDetail(error + ex.getMessage()).build();
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * 处理自定义异常，将自定义异常返回给客户端API
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @ExceptionHandler(CustomServiceException.class)
    protected ResponseEntity<Object> handleCustomAPIException(
            HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        ApiErrorResponse response = new ApiErrorResponse.ApiErrorResponseBuilder()
                .withStatus(status)
                .withError_code(HttpStatus.NOT_FOUND.name())
                .withMessage(ex.getLocalizedMessage())
                .withDetail(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, response.getStatus());
    }

}
