package com.coupon.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 基于SpringBoot框架的个人练手项目-
 *
 * @author JMF
 * @date 2026-04-05 16:30
 * @date 2026-04-05
 */
@ConfigurationProperties(prefix = "wechat.mini")
@Component
@Data
public class WechatMiniConfig {
    private String appId;
    private String appSecret;
}
