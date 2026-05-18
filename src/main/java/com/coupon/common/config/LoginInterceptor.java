package com.coupon.common.config;

import com.coupon.common.RedisConstant;
import com.coupon.common.enums.CodeEnum;
import com.coupon.common.Result;
import com.coupon.dto.MemberDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Configuration
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    private String env="test";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //在redis中保存：以token作为key的用户信息,和以用户id为key存最新token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer")) {
            token = token.substring(7);
        }
        response.setContentType("application/json;charset=utf-8");
        if (StringUtils.isEmpty(token)) {
            response.getWriter().write(objectMapper.writeValueAsString(Result.fail(CodeEnum.Forbidden, "请先登录")));
            return false;
        }
        String json=redisTemplate.opsForValue().get(RedisConstant.LOGIN_TOKEN +token);
        if(StringUtils.isEmpty(json)){
            response.getWriter().write(objectMapper.writeValueAsString(Result.fail(CodeEnum.Forbidden, "登录已过期，请重新登录")));
            return false;
        }
        MemberDTO dto = objectMapper.readValue(json, MemberDTO.class);
        String loginToken = redisTemplate.opsForValue().get(RedisConstant.LOGIN_OPENID + dto.getOpenid());
        if(dto.getNickname().equals("admin")&&env.equals("test")){
            //如果当前登录用户是admin并且是测试环境，就直接放行
            return true;
        }
        if(!token.equals(loginToken)){
            response.getWriter().write(objectMapper.writeValueAsString(Result.fail(CodeEnum.Forbidden, "登录凭证已失效")));
            return false;
        }
        return true;
    }
}
