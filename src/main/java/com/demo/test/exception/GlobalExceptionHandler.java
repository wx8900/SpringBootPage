package com.demo.test.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
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

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常捕捉统一处理类
 *
 * @author Jack
 * @date 2019/07/17
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    static Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    /*private static Properties properties =
      ReadPropertiesUtil.getProperties(System.getProperty("user.dir")
      + CommonUrl.RESPONSE_PROP_URL);*/

    /**
     * 构造异常堆栈信息
     *
     * @param ex
     * @return
     */
    public static String buildErrorMessage(Throwable ex) {
        String stackTrace = getStackTraceString(ex);
        String exceptionType = ex.toString();
        String exceptionMessage = ex.getMessage();
        return String.format("%s : %s \r%n %s", exceptionType, exceptionMessage, stackTrace);
    }

    /**
     * 打印异常堆栈信息
     *
     * @param ex
     * @return
     */
    public static String getStackTraceString(Throwable ex) {
        final int minSize = 2048 * 2048 * 96;
        String result = "";
        StringBuilder traceBuilder = null;
        StackTraceElement[] traceElements = ex.getStackTrace();

        if (traceElements != null && traceElements.length > 0) {
            traceBuilder = new StringBuilder(traceElements.length);
            for (StackTraceElement traceElement : traceElements) {
                traceBuilder.append(traceElement.toString()).append("\n");
            }
        }
        result = traceBuilder.toString();

        return (result.length() < minSize) ? result : result.substring(0, minSize * 2048);
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
        StringBuffer sb = new StringBuffer(1024);
        sb.append(buildErrorMessage(exception));
        String detail = sb.toString();
        logger.error("GlobalExceptionHandler：" + exception.getLocalizedMessage());
        logger.error("GlobalExceptionHandler：" + exception.getCause());
        logger.error("GlobalExceptionHandler：" + exception.getSuppressed().toString());
        logger.error("GlobalExceptionHandler：" + exception.getMessage());
        logger.error("GlobalExceptionHandler =========> get StackTrace Info : "
                + detail);

        ApiErrorResponse apiError = new ApiErrorResponse();
        apiError.setStatus(HttpStatus.SERVICE_UNAVAILABLE);
        apiError.setCode("503");
        apiError.setMessage("现在服务器报非自定义异常了，请马上联系管理员！");
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
        logger.error("myExceptionHandler：" + exception.getSuppressed().toString());
        logger.error("myExceptionHandler：" + exception.getMessage());
        logger.error("myExceptionHandler：" + exception.getStackTrace().toString());
        logger.error("myExceptionHandler ======> get StackTrace Info : "
                + detail);

        ApiErrorResponse apiError = new ApiErrorResponse();
        apiError.setStatus(HttpStatus.EXPECTATION_FAILED);
        apiError.setCode("417");
        apiError.setMessage("现在服务器报自定义异常，请马上联系管理员！");
        return apiError;
    }

    /**
     * 通用的接口映射异常处理
     *
     * @param ex
     * @param body
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) ex;
            return new ResponseEntity<>(new ApiErrorResponse(status, "400"
                    , exception.getBindingResult().getAllErrors().get(0).getDefaultMessage(), ""), status);
        }
        if (ex instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException exception = (MethodArgumentTypeMismatchException) ex;
            logger.error("参数转换失败，方法：" + exception.getParameter().getMethod().getName() + "，参数：" + exception.getName()
                    + "" +
                    ",信息：" + exception.getLocalizedMessage());
            return new ResponseEntity<>(
                    new ApiErrorResponse(status, "400", "参数转换失败", ""), status);
        }
        return new ResponseEntity<>(new ApiErrorResponse(status, "400",
                "参数转换失败", ""), status);
    }

    /**
     * 异常捕获
     *
     * @param //e 捕获的异常
     * @return 封装的返回对象
     **/
    /*@ExceptionHandler(Exception.class)
    public ApiErrorResponse handlerException(Throwable e) {
        ApiErrorResponse returnVO = new ApiErrorResponse();
        String errorName = e.getClass().getName();
        errorName = errorName.substring(errorName.lastIndexOf(".") + 1);
        //如果没有定义异常，而是直接抛出一个运行时异常，需要进入以下分支
        if (e.getClass() == RuntimeException.class) {
            returnVO.setMessage(properties.getProperty(valueOf("RuntimeException").msg()) +": "+ e.getMessage());
            returnVO.setError_code(properties.getProperty(valueOf("RuntimeException").val()));
        } else {
            returnVO.setMessage(properties.getProperty(valueOf(errorName).msg()));
            returnVO.setError_code(properties.getProperty(valueOf(errorName).val()));
        }
        return returnVO;
    }*/
    @ExceptionHandler(CustomNotFoundException.class)
    public ApiErrorResponse handleNotFoundException(CustomNotFoundException ex) {

        ApiErrorResponse response = new ApiErrorResponse.ApiErrorResponseBuilder()
                .status(HttpStatus.NOT_FOUND)
                .code("NOT_FOUND")
                .message(ex.getLocalizedMessage()).build();

        return response;
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
            HttpMessageNotReadableException ex, com.google.common.net.HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request ";
        ApiErrorResponse response = new ApiErrorResponse.ApiErrorResponseBuilder()
                .status(status)
                .code("BAD_DATA")
                .message(ex.getLocalizedMessage())
                .detail(error + ex.getMessage()).build();
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
            HttpMessageNotReadableException ex, com.google.common.net.HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        ApiErrorResponse response = new ApiErrorResponse.ApiErrorResponseBuilder()
                .status(status)
                .code(HttpStatus.NOT_FOUND.name())
                .message(ex.getLocalizedMessage())
                .detail(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, response.getStatus());
    }

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
            MethodArgumentNotValidException ex, com.google.common.net.HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        ApiErrorResponse response = new ApiErrorResponse.ApiErrorResponseBuilder()
                .status(status)
                .code(HttpStatus.BAD_REQUEST.name())
                .message(ex.getLocalizedMessage()).build();

        return new ResponseEntity<>(response, response.getStatus());
    }

}
