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
@Api(tags = "消息队列名称")

public enum QueueNameEnum {
    HELLO_QUEUE("hello_queue", "发送欢迎消息的队列"),
    RETRY_QUEUE("retry_queue", "通用消息重试队列，队列延迟3秒");

    @EnumValue
    private final String name;
    private final String desc;

    QueueNameEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public static QueueNameEnum getByCode(String name) {
        for (QueueNameEnum type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return null;
    }
}
