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
@Api(tags = "路由键/绑定键名称")

public enum RoutingKeyNameEnum {
    HELLO_ROUTING_KEY("user.hello", "发送欢迎消息路由键"),
    RETRY_HELLO_ROUTING_KEY("retry.hello.*", "欢迎消息重试路由键");

    @EnumValue
    private final String name;
    private final String desc;

    RoutingKeyNameEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public static RoutingKeyNameEnum getByCode(String name) {
        for (RoutingKeyNameEnum type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return name;
    }
}
