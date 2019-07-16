package com.demo.test.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理类
 *
 * @author Jack
 * @date   2019/07/16
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    static Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * 构造异常堆栈信息
     *
     * @param ex
     * @return
     */
    public static String buildErrorMessage(Exception ex) {
        String result;
        String stackTrace = getStackTraceString(ex);
        String exceptionType = ex.toString();
        String exceptionMessage = ex.getMessage();
        result = String.format("%s : %s \r\n %s", exceptionType, exceptionMessage, stackTrace);
        return result;
    }

    /**
     * 打印异常堆栈信息
     *
     * @param ex
     * @return
     */
    public static String getStackTraceString(Throwable ex) {//(Exception ex) {
        StackTraceElement[] traceElements = ex.getStackTrace();
        StringBuilder traceBuilder = new StringBuilder(1500);
        if (traceElements != null && traceElements.length > 0) {
            for (StackTraceElement traceElement : traceElements) {
                traceBuilder.append(traceElement.toString()).append("\n");
            }
        }
        return traceBuilder.toString().substring(0, 1450);
    }

    /**
     * 拦截捕捉所有异常
     *
     * @param request
     * @param exception
     * @return
     * @throws Exception
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ApiErrorResponse allExceptionHandler(
            HttpServletRequest request, Exception exception) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(buildErrorMessage(exception));
        String detail = sb.toString();
        logger.error("GlobalExceptionHandler：" + exception.getLocalizedMessage());
        logger.error("GlobalExceptionHandler：" + exception.getCause());
        logger.error("GlobalExceptionHandler：" + exception.getSuppressed());
        logger.error("GlobalExceptionHandler：" + exception.getMessage());
        logger.error("GlobalExceptionHandler：" + exception.getStackTrace());
        logger.error("GlobalExceptionHandler ======> get StackTrace Info : "
                + detail);

        ApiErrorResponse apiError = new ApiErrorResponse();
        apiError.setStatus(HttpStatus.SERVICE_UNAVAILABLE);
        apiError.setError_code("500");
        apiError.setMessage("现在服务器报非自定义异常了，请马上联系管理员！");
        //apiError.setDetail(detail);
        return apiError;
    }

    /**
     * 拦截捕捉自定义异常
     *
     * @param request
     * @param exception
     * @return
     * @throws Exception
     */
    @ResponseBody
    @ExceptionHandler(value = ApiErrorResponse.class)
    public ApiErrorResponse myExceptionHandler(
            HttpServletRequest request, Exception exception) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(buildErrorMessage(exception));
        String detail = sb.toString();
        logger.error("myExceptionHandler：" + exception.getLocalizedMessage());
        logger.error("myExceptionHandler：" + exception.getCause());
        logger.error("myExceptionHandler：" + exception.getSuppressed());
        logger.error("myExceptionHandler：" + exception.getMessage());
        logger.error("myExceptionHandler：" + exception.getStackTrace());
        logger.error("myExceptionHandler ======> get StackTrace Info : "
                + detail);

        ApiErrorResponse apiError = new ApiErrorResponse();
        apiError.setStatus(HttpStatus.SERVICE_UNAVAILABLE);
        apiError.setError_code("400");
        apiError.setMessage("现在服务器报自定义异常，请马上联系管理员！");
        //apiError.setDetail(detail);
        return apiError;
    }

}
