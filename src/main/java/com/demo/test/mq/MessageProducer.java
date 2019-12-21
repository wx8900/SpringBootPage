package com.demo.test.mq;

import com.demo.test.domain.Student;
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
public class MessageProducer {

    static Logger logger = LogManager.getLogger(MessageProducer.class);

    /**
     * 注入AmqpTemplate，然后利用AmqpTemplate向一个名为XXX的消息队列中发送消息。
     */
    @Autowired
    private AmqpTemplate amqpTemplate;

    private Student student;

    public void sendMessage(String message) throws Exception {
        logger.info("单对单发送。Producer发出了消息:" + message);
        amqpTemplate.convertAndSend(RabbitConfiguration.EXCHANGE_DIRECT,
                RabbitConfiguration.DIRECT_ROUTING_KEY_SENDQUEUE, message);
    }

    @RabbitListener(queues = RabbitConfiguration.QUEUE_RECEIVE)
    @RabbitHandler
    public void onMessage(Student student) {
        this.student = student;
        logger.info("单对单返回参数。从数据库得到信息并返回:$$$$$$$$$$$$$$========>" + student.toString());
    }

    public Student getStudent() {
        return student;
    }
}
