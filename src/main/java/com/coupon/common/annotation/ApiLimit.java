package com.coupon.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 基于SpringBoot框架的个人练手项目-redis限流
 *
 * @author JMF
 * @date 2026-04-06 08:30
 * @date 2026-04-06
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiLimit {
    //按照ip限流，默认100次
    int ipLimit() default 100;

    //按照用户openId限流
    int openLimit() default 10;
}
