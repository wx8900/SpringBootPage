package com.demo.test.controllers;

import com.demo.test.exception.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test class for the catching and handler global Exceptions and customized Exceptions
 *
 * @author Jack
 * @date 2019/07/16
 */
@RestController
public class TestGlobalExceptionController {

    /**
     * test exceptions of myExceptionHandler
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/myException")
    public String index() throws Exception {
        throw ApiErrorResponse.builder().status(HttpStatus.BAD_REQUEST).code("empty")
                .detail("/API/getUserName").message("在获取用户名字的时候为空").build();
    }

    /**
     * test exceptions of GlobalExceptionHandler
     *
     * @return
     */
    @RequestMapping("/byzero")
    public String test() {
        int id = 10;
        if (true) {
            // open when do testing
            //id = 1 / 0;
        }
        return "success";
    }

    /**
     * open when do testing
     *
     * @return
     */
    @RequestMapping("/test")
    public String testZero() {
        Object msg = null;
        // open when do testing
        //msg.toString();
        createException();
        return "success";
    }

    /**
     * open when do testing
     */
    private void createException() {
        // open when do testing
        // int i = 5 / 0;
    }
}
