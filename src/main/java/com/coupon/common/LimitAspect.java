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

@Aspect
@Component
@Order(1)
public class LimitAspect {

    private static final int OPEN_LIMIT = 10;

    private static final long LIMIT_WINDOW_SECONDS = 1;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Pointcut("@annotation(com.coupon.common.annotation.ApiLimit)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        ApiLimit apiLimit = signature.getMethod().getAnnotation(ApiLimit.class);
        int urlLimitCount = apiLimit.urlLimit();

        HttpServletRequest request = getRequest();
        String method = request.getMethod();
        String uri = request.getRequestURI();

        checkUrlLimit(method, uri, urlLimitCount);

        String token = getToken();
        if (token != null) {
            checkOpenLimit(token);
        }
    }

    private void checkUrlLimit(String method, String uri, int urlLimitCount) {
        String key = "limit:url:" + method + ":" + uri;
        Long count = redisTemplate.opsForValue().increment(key, 1);
        if (count != null && count == 1) {
            redisTemplate.expire(key, LIMIT_WINDOW_SECONDS, TimeUnit.SECONDS);
        }
        if (count != null && count > urlLimitCount) {
            throw new ReturnException("接口访问过于频繁，请稍后重试");
        }
    }

    private void checkOpenLimit(String token) {
        String key = "limit:openId:" + token;
        Long count = redisTemplate.opsForValue().increment(key, 1);
        if (count != null && count == 1) {
            redisTemplate.expire(key, LIMIT_WINDOW_SECONDS, TimeUnit.SECONDS);
        }
        if (count != null && count > OPEN_LIMIT) {
            throw new ReturnException("请求过于频繁");
        }
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new ReturnException("无法获取请求上下文");
        }
        return attributes.getRequest();
    }

    private String getToken() {
        HttpServletRequest request = getRequest();
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer")) {
            token = token.substring(7);
        }
        return token;
    }
}
