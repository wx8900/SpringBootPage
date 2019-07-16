package com.demo.test.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 *
 * @author Jack
 * @date 2019/07/17
 */

@Controller
@RequestMapping("/exceptionTest")
@ResponseBody
public class ExceptionTest {
    @RequestMapping("/test")
    public String test(){
        Object msg = null;
        msg.toString();
        createException();
        return "我是正常的";
    }

    private void createException(){
        int i = 5 / 0;
    }
}
