package com.coupon.common.util;

import com.coupon.common.exception.ReturnException;
import com.coupon.common.RedisConstant;
import com.coupon.dto.MemberDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 基于SpringBoot框架的个人练手项目-会员信息工具类
 *
 * @author JMF
 * @date 2026-04-11 11:18
 * @date 2026-04-11
 */
@Component
public class MemberUtil {
    @Autowired
    private  static StringRedisTemplate redisTemplate;

    @Autowired
    private static ObjectMapper objectMapper;

    public static MemberDTO getMemberInfo(){
        String json=getToken();
        try {
            return objectMapper.readValue(json,MemberDTO.class);
        } catch (JsonProcessingException e) {
            throw new ReturnException("会员详情获取失败");
        }
    }

    private static String getToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new ReturnException("无法获取请求上下文");
        }
        String token = attributes.getRequest().getHeader("Authorization");
        if (token != null && token.startsWith("Bearer")) {
            token = token.substring(7);
        }

        String json = redisTemplate.opsForValue().get(RedisConstant.LOGIN_TOKEN + token);
        return json;
    }
}
