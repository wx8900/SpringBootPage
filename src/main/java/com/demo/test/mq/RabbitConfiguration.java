package com.demo.test.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置一个队列
 *
 * @author Jack
 */
@Configuration
public class RabbitConfiguration {

    public static final String QUEUE_SEND = "QUEUE_SEND";
    public static final String QUEUE_RECEIVE = "QUEUE_RECEIVE";
    public static final String DIRECT_ROUTING_KEY_RECVQUEUE = "recvqueue";
    public static final String DIRECT_ROUTING_KEY_SENDQUEUE = "sendqueue";
    public static final String EXCHANGE_DIRECT = "directExchange";

    /**
     * 创建队列 - 发送队列
     * 第二个参数： true 是否持久
     *
     * @return
     */
    @Bean
    public Queue sendQueue() {
        return new Queue(QUEUE_SEND);
    }

    /**
     * 创建队列 - 读取队列
     *
     * @return
     */
    @Bean
    public Queue recvQueue() {
        return new Queue(QUEUE_RECEIVE);
    }

    /**
     * 创建交换器
     * 参数列表 ： 交换器名称、是否持久化、是否自动删除
     *
     * @return
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_DIRECT);
    }

    /**
     * 队列绑定并关联到RoutingKey
     *
     * @return
     */
    @Bean
    public Binding bindingSender() {
        return BindingBuilder.bind(sendQueue()).to(directExchange()).with(RabbitConfiguration.DIRECT_ROUTING_KEY_SENDQUEUE);
    }

    @Bean
    public Binding bindingReceiver() {
        return BindingBuilder.bind(recvQueue()).to(directExchange()).with(RabbitConfiguration.DIRECT_ROUTING_KEY_RECVQUEUE);
    }

    /*@Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }*/

}
