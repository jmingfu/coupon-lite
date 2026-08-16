package com.coupon.common.util;

import com.coupon.common.enums.RoutingKeyNameEnum;
import com.coupon.dto.MemberDTO;
import com.coupon.service.MemberLoginLogService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * 基于SpringBoot框架的个人练手项目-MQ消费者
 *
 * @author JMF
 * @date 2026-08-02 18:51
 * @date 2026-08-02
 */
@Component
public class MessageConsumerUtil {
    @Autowired
    MemberLoginLogService memberLoginLogService;

    @Autowired
    MessageRetryUtil messageRetryUtil;

    @RabbitListener(queues = "hello_queue")
    public void sayHello(MemberDTO memberDTO, @Header("retryCount") Integer retryCount) {
        boolean success = false;
        if (!success) {
            //messageRetryUtil.messageRetryByDead(memberDTO, RoutingKeyNameEnum.RETRY_HELLO_ROUTING_KEY.getName(), retryCount);
            messageRetryUtil.messageRetryByDelay(memberDTO,RoutingKeyNameEnum.RETRY_HELLO_ROUTING_KEY.getName(),retryCount,3000);
            return;
        }
        System.out.println(memberLoginLogService.updateLoginLog(memberDTO) ? "登录记录写入成功" : "登录记录写入失败");
    }
}
