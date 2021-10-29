package com.changgou.order.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    /***
     * 创建queue1
     * @return
     */
    @Bean
    public Queue orderDelayQueue() {
        return QueueBuilder.durable("orderListenerQueue")
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", "orderListenerQueue")
                .build();

    }

    /***
     * 创建队列
     * @return
     */
    @Bean
    public Queue orderListenerQueue() {
        return QueueBuilder.durable("orderListenerQueue").withArgument("x-dead-letter-exchange", "").withArgument("x-dead-letter-routing-key", "orderListenerQueue").build();
    }

    /****
     * 交换机
     * @return
     */
    @Bean
    public Exchange orderListenerExchange() {
        return new DirectExchange("orderListenerExchange");
    }

    @Bean
    public Binding orderListenerBinding(Queue orderListenerQueue, Exchange orderListenerExchange) {
        return BindingBuilder.bind(orderListenerQueue).to(orderListenerExchange).with("orderListenerQueue").noargs();
    }
}
