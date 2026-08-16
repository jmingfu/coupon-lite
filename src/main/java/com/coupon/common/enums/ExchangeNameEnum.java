package com.coupon.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.annotations.Api;

/**
 * 基于SpringBoot框架的个人练手项目-优惠券类型
 *
 * @author JMF
 * @date 2026-04-08 12:59
 * @date 2026-04-08
 */
@Api(tags = "交换机名称")

public enum ExchangeNameEnum {
    HELLO_DIRECT_EXCHANGE("coupon.message", "发送欢迎消息的交换机"),
    RETRY_TOP_EXCHANGE("coupon.retry", "通用消息重试主题交换机"),
    DEAD_MESSAGE_DIRECT_EXCHANGE("coupon.dead","死信交换机"),
    DELAY_EXCHANGE("coupon.delay","延迟交换机");

    @EnumValue
    private final String name;
    private final String desc;

    ExchangeNameEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public static ExchangeNameEnum getByCode(String name) {
        for (ExchangeNameEnum type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return null;
    }
}
