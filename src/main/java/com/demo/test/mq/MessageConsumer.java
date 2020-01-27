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

    static Logger logger = LogManager.getLogger(MessageConsumer.class);

    @Autowired
    UserService userService;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @RabbitListener(queues = RabbitConfiguration.DIRECT_ROUTING_KEY_SENDQUEUE)
    @RabbitHandler
    public void onMessage(String message) {
        Student student;
        try {
            student = getStudentFromDB(message);
            //logger.info("单对单传递消息。Consumer收到消息 : " + message + "===================>" + student.toString());
            amqpTemplate.convertAndSend(RabbitConfiguration.EXCHANGE_DIRECT,
                    RabbitConfiguration.DIRECT_ROUTING_KEY_RECVQUEUE, student);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private Student getStudentFromDB(String message) {
        return userService.getUser(Long.valueOf(message)).orElse(new Student());
    }

}
