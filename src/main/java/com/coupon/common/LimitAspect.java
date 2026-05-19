package com.coupon.common;

import com.coupon.common.annotation.ApiLimit;
import com.coupon.common.exception.ReturnException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 基于SpringBoot框架的个人练手项目-
 *
 * @author JMF
 * @date 2026-04-06 08:47
 * @date 2026-04-06
 */

@Aspect
@Component
@Order(1)
public class LimitAspect {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Pointcut("@annotation(com.coupon.common.annotation.ApiLimit)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        ApiLimit apiLimit = signature.getMethod().getAnnotation(ApiLimit.class);
        int openCount = apiLimit.openLimit();
        int ipCount = apiLimit.ipLimit();
        String token = getToken(), ip = getIp();
        String key = "";
        Long count;
        try {
            if (token != null) {
                key = "limit:openId:" + token;
                count = redisTemplate.opsForValue().increment(key, 1);
                if (count == 1) {
                    redisTemplate.expire(key, 1, TimeUnit.SECONDS);
                }
                if (count > openCount) {
                    throw new ReturnException("请求过于频繁");
                }
            } else {
                key = "limit:ip:" + ip;
                count = redisTemplate.opsForValue().increment(key, 1);
                if (count == 1) {
                    redisTemplate.expire(key, 1, TimeUnit.SECONDS);
                }
                if (count > ipCount) {
                    throw new ReturnException("网络繁忙，请稍后重试");
                }
            }
        } catch (Exception e) {
            throw new ReturnException("服务器出错,错误信息,aop:" + e.getMessage());
        }
    }

    private String getIp() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        return request.getRemoteAddr();
    }

    private String getToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer")) {
            token = token.substring(7);
        }
        return token;
    }

}
