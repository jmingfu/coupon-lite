package com.coupon.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基于SpringBoot框架的个人练手项目-RabbitMQ配置
 *
 * @author JMF
 * @date 2026-08-02 17:27
 * @date 2026-08-02
 */
@Configuration
public class RabbitMQConfig {

    //声明直连交换机
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("coupon.message", true, false);
    }

    @Bean
    public Queue helloQueue() {
        return new Queue("hello_queue", true);
    }

    @Bean
    public Binding bindingHelloQueue(Queue helloQue, DirectExchange directExchange) {
        return BindingBuilder.bind(helloQue).to(directExchange).with("user.hello");
    }
}
