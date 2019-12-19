package com.demo.test.mq;

import com.demo.test.domain.Student;
import com.demo.test.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jack
 */
@Component
public class MessageConsumer {

    @Autowired
    UserService userService;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @RabbitListener(queues = RabbitConfiguration.DIRECT_ROUTING_KEY_SENDQUEUE)
    @RabbitHandler
    public void onMessage(String message) {
        System.out.println("单对单发送参数。Consumer收到了消息:=================>" + message);
        Student student;
        try {
            student = userService.getUser(Long.valueOf(message)).orElse(new Student());
            System.out.println("单对单返回参数。student.toString():=================>" + student.toString());
            amqpTemplate.convertAndSend(RabbitConfiguration.EXCHANGE_DIRECT, RabbitConfiguration.DIRECT_ROUTING_KEY_RECVQUEUE, student);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
