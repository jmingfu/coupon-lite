package com.coupon.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 基于SpringBoot框架的个人练手项目-配置类
 *
 * @author JMF
 * @date 2026-04-05 19:54
 * @date 2026-04-05
 */
//@Api(tags = "")
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
