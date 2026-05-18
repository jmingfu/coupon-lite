package com.coupon.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 基于SpringBoot框架的个人练手项目-
 *
 * @author JMF
 * @date 2026-04-07 10:16
 * @date 2026-04-07
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx.login")
public class WechatLoginProperties {
    private String signSecret;
    private int signExpireSeconds;
    private int nonceExpireSeconds;
}
