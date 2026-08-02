package com.coupon.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coupon.common.RedisConstant;
import com.coupon.common.config.WechatLoginProperties;
import com.coupon.common.config.WechatMiniConfig;
import com.coupon.common.enums.MemberStatusEnum;
import com.coupon.common.exception.ReturnException;
import com.coupon.controller.MemberController;
import com.coupon.dto.MemberDTO;
import com.coupon.dto.WechatCode2SessionRes;
import com.coupon.entity.Member;
import com.coupon.entity.MemberLoginLog;
import com.coupon.mapper.MemberLoginLogMapper;
import com.coupon.mapper.MemberMapper;
import com.coupon.service.MemberLoginLogService;
import com.coupon.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 基于SpringBoot框架的个人练手项目-
 *
 * @author JMF
 * @date 2026-04-05 16:24
 * @date 2026-04-05
 */
@Service
public class MemberLoginLogServiceImpl implements MemberLoginLogService {
    @Autowired
    MemberLoginLogMapper loginLogMapper;

    @Override
    public boolean updateLoginLog(MemberDTO memberDTO) {
        MemberLoginLog memberLoginLog = new MemberLoginLog();
        if (memberDTO.getId() == null) {
            throw new ReturnException("用户id不存在！");
        }
        memberLoginLog.setMemberId(memberDTO.getId());
        memberLoginLog.setWelcomeMsg((memberDTO.getNickname() == null ? memberDTO.getOpenid() : memberDTO.getNickname()) + "欢迎登录");
        return loginLogMapper.insert(memberLoginLog) == 1;
    }
}
