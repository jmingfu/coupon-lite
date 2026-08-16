package com.coupon.common.util;

import com.coupon.common.enums.ExchangeNameEnum;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 基于SpringBoot框架的个人练手项目-
 *
 * @author JMF
 * @date 2026-08-14 15:45
 * @date 2026-08-14
 */
@Component
public class MessageRetryUtil {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 通过死信交换机重试消息
     * @param messageBody 消息体
     * @param routingKey 路由键
     * @param retryCount 剩余重试次数
     */
    public void messageRetryByDead(Object messageBody, String routingKey, Integer retryCount) {
        if (retryCount > 0) {
            rabbitTemplate.convertAndSend(ExchangeNameEnum.RETRY_TOP_EXCHANGE.getName(), routingKey, messageBody, message -> {
                message.getMessageProperties().setHeader("retryCount", retryCount - 1);
                System.out.println("当前消息已重试" + (4 - retryCount) + "次");
                return message;
            });
        } else {
            System.out.println("消息重试次数超过限制！");
        }
    }

    /**
     * 通过延迟交换机重试消息
     * @param messageBody 消息体
     * @param routingKey 路由键
     * @param retryCount 剩余重试次数
     */
    public void messageRetryByDelay(Object messageBody, String routingKey, Integer retryCount,long delayMills) {
        if (retryCount > 0) {
            rabbitTemplate.convertAndSend(ExchangeNameEnum.DELAY_EXCHANGE.getName(), routingKey, messageBody, message -> {
                message.getMessageProperties().setHeader("retryCount", retryCount - 1);
                message.getMessageProperties().setDelay((int) delayMills);
                System.out.println("当前消息已重试" + (4 - retryCount) + "次");
                return message;
            });
        } else {
            System.out.println("消息重试次数超过限制！");
        }
    }
}
