package com.coupon.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor()).
                addPathPatterns("/**").
                excludePathPatterns(
                // 管理员登录注册
                "/api/v1/admin/login",
                "/api/v1/admin/register",
                // 小程序登录或注册
                "/api/v1/member/login-or-register",
                // 未登录用户也可以查看优惠券
                "/api/v1/coupon/page",
                // Swagger 静态资源
                "/webjars/**",
                "/swagger-resources/**",
                "/swagger-ui.html",
                // Swagger API 文档接口
                "/v2/api-docs/**",
                // 前端页面和静态资源
                "/**.html",
                "/js/**",
                "/css/**",
                "/img/**",
                "/favicon.ico");
    }
}
