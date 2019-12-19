package com.demo.test.controllers;

import com.demo.test.mq.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/v1/api/students/rabbitmq")
public class MyRabbitmqController {

    @Autowired
    MessageProducer sender;

    @RequestMapping("/sender")
    @ResponseBody
    public String sender(){
        System.out.println("send string:hello world");
        //sender.send("hello world");
        return "sending...";
    }
}
