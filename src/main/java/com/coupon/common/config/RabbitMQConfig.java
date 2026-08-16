package com.coupon.common.config;

import com.coupon.common.enums.ExchangeNameEnum;
import com.coupon.common.enums.QueueNameEnum;
import com.coupon.common.enums.RoutingKeyNameEnum;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于SpringBoot框架的个人练手项目-RabbitMQ配置
 *
 * @author JMF
 * @date 2026-08-02 17:27
 * @date 2026-08-02
 */
@Configuration
public class RabbitMQConfig {

    //发送欢迎消息的直连交换机
    @Bean
    public DirectExchange helloDirectExchange() {
        return new DirectExchange(ExchangeNameEnum.HELLO_DIRECT_EXCHANGE.getName(), true, false);
    }

    //发送重试消息的主题交换机
    @Bean
    public DirectExchange retryDirectExchange() {
        return new DirectExchange(ExchangeNameEnum.RETRY_TOP_EXCHANGE.getName(), true, false);
    }

    //路由死信消息的死信交换机
    @Bean
    public DirectExchange deadMessageDirectExchange() {
        return new DirectExchange(ExchangeNameEnum.DEAD_MESSAGE_DIRECT_EXCHANGE.getName(), true, false);
    }

    //延迟交换机
    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(ExchangeNameEnum.DELAY_EXCHANGE.getName(), "x-delayed-message", true, false, args);
    }

    @Bean
    public Queue helloQueue() {
        return new Queue(QueueNameEnum.HELLO_QUEUE.getName(), true);
    }

    @Bean
    public Queue retryQueue() {
        return QueueBuilder.durable(QueueNameEnum.RETRY_QUEUE.getName())
                .withArgument("x-dead-letter-exchange", ExchangeNameEnum.DEAD_MESSAGE_DIRECT_EXCHANGE.getName())
                .withArgument("x-message-ttl", 3000).build();
    }

    @Bean
    public Binding bindingHelloWithHelloQueue(@Qualifier("helloQueue") Queue helloQue, DirectExchange helloDirectExchange) {
        return BindingBuilder.bind(helloQue).to(helloDirectExchange).with(RoutingKeyNameEnum.HELLO_ROUTING_KEY);
    }

    //绑定重试交换机和重试队列
    @Bean
    public Binding bindingHelloWithRetryQueue(@Qualifier("retryQueue") Queue retryQueue, DirectExchange retryDirectExchange) {
        return BindingBuilder.bind(retryQueue).to(retryDirectExchange).with(RoutingKeyNameEnum.RETRY_HELLO_ROUTING_KEY);
    }

    //绑定死信交换机和业务队列
    @Bean
    public Binding bindingDeadWithHelloQueue(@Qualifier("helloQueue") Queue helloQueue, DirectExchange deadMessageDirectExchange) {
        return BindingBuilder.bind(helloQueue).to(deadMessageDirectExchange).with(RoutingKeyNameEnum.RETRY_HELLO_ROUTING_KEY);
    }

    //绑定延迟交换机和业务队列
    @Bean
    public Binding bindDelayWithHelloQueue(@Qualifier("helloQueue") Queue helloQueue, CustomExchange delayExchange) {
        return BindingBuilder.bind(helloQueue).to(delayExchange).with(RoutingKeyNameEnum.RETRY_HELLO_ROUTING_KEY).noargs();
    }

}
