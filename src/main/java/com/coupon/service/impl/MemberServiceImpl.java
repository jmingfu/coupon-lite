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
import com.coupon.mapper.MemberMapper;
import com.coupon.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
public class MemberServiceImpl implements MemberService {
    @Autowired
    private WechatMiniConfig wechatMiniConfig;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WechatLoginProperties wechatLoginProperties;

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);


    @Override
    @Transactional(rollbackFor = Exception.class)
    public MemberDTO wxLogin(MemberDTO memberDTO) throws Exception {
        if (!checkSign(memberDTO.getSign(), memberDTO.getCode(), memberDTO.getNonce(), memberDTO.getTimeStamp())) {
            throw new ReturnException("非法请求");
        }
        String url = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                wechatMiniConfig.getAppId(),
                wechatMiniConfig.getAppSecret(),
                memberDTO.getCode());
        String jsonStr = restTemplate.getForObject(url, String.class);
        WechatCode2SessionRes res = objectMapper.readValue(jsonStr, WechatCode2SessionRes.class);
        if (res == null || StringUtils.isNotBlank(res.getErrCode())) {
            log.error("小程序授权失败" + res.getErrMsg());
        }
        String openId = res.getOpenid();
        if (StringUtils.isBlank(openId)) {
            throw new ReturnException("用户openId获取失败");
        }
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Member::getOpenid, openId);
        Member member = memberMapper.selectOne(wrapper);
        String token = UUID.randomUUID().toString().replace("-", "");
        if (member != null) {
            // 检查会员状态是否禁用
            if (member.getStatus() != null && member.getStatus() == MemberStatusEnum.DISABLED) {
                throw new ReturnException("账号已被禁用");
            }
            BeanUtils.copyProperties(member, memberDTO);
        } else {
            Member newMember = new Member();
            newMember.setOpenid(openId);
            newMember.setNickname(memberDTO.getNickname());
            newMember.setPhone(memberDTO.getPhone());
            newMember.setStatus(MemberStatusEnum.NORMAL); // 默认正常状态
            try {
                memberMapper.insert(newMember);
                BeanUtils.copyProperties(newMember, memberDTO);
            } catch (Exception e) {
                log.error("插入失败，原因:" + e.getMessage());
            }
        }
        memberDTO.setToken(token);
        String oldToken = stringRedisTemplate.opsForValue().get(RedisConstant.LOGIN_OPENID + memberDTO.getOpenid());
        if (StringUtils.isNotBlank(oldToken)) {
            stringRedisTemplate.delete(RedisConstant.LOGIN_TOKEN + oldToken);
        }
        //按照id存token，按照token存用户信息
        stringRedisTemplate.opsForValue().set(RedisConstant.LOGIN_OPENID + memberDTO.getOpenid(), token);
        stringRedisTemplate.opsForValue().set(RedisConstant.LOGIN_TOKEN + token, objectMapper.writeValueAsString(memberDTO));
        return memberDTO;
    }

    public Boolean checkSign(String sign, String code, String nonce, String timestamp) {
//        long now = System.currentTimeMillis() / 1000;
//        long ts;
//        try {
//            ts=Long.parseLong(timestamp);
//        } catch (NumberFormatException e) {
//            throw new ReturnException("时间戳格式错误");
//        }
//        //从配置读取过期时间
//        if(Math.abs(now-ts)>wechatLoginProperties.getSignExpireSeconds()){
//            throw new ReturnException("请求已过期");
//        }

        String plainText = "code=" + code + "&timestamp=" + timestamp + "&nonce=" + nonce + "&secret=" + wechatLoginProperties.getSignSecret();
        String md5 = SecureUtil.md5(plainText);
        System.out.println(md5);
        //防重放
        String nonceKey = "LOGIN:nonce" + nonce;
        Boolean absent = stringRedisTemplate.opsForValue().setIfAbsent(nonceKey, "1", wechatLoginProperties.getNonceExpireSeconds(), TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(absent)) {
            throw new ReturnException("请求过于频繁，请稍后重试");
        }
        return md5.equals(sign);
    }

    @Override
    public MemberDTO getById(Long id) {
        Member member = memberMapper.selectById(id);
        MemberDTO memberDTO = new MemberDTO();
        BeanUtils.copyProperties(member, memberDTO);
        return memberDTO;
    }

    @Override
    public IPage<MemberDTO> selectPage(MemberDTO dto) {
        Page<Member> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(dto.getPhone()), Member::getPhone, dto.getPhone());
        wrapper.like(StringUtils.isNotEmpty(dto.getNickname()), Member::getNickname, dto.getNickname());
        //待定，是否有优惠券
        // TODO: 2026/4/6
        wrapper.between(dto.getBeginTime() != null && dto.getEndTime() != null
                , Member::getCreateTime, dto.getBeginTime(), dto.getEndTime());
        Page<Member> memberPage = memberMapper.selectPage(page, wrapper);
        return memberPage.convert(member -> {
            MemberDTO memberDTO = new MemberDTO();
            BeanUtils.copyProperties(member, memberDTO);
            return memberDTO;
        });
    }

    /**
     * 禁用/启用会员
     * @param memberId 会员ID
     * @param status 目标状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMemberStatus(Long memberId, MemberStatusEnum status) {
        Member member = memberMapper.selectById(memberId);
        if (member == null) {
            throw new ReturnException("会员不存在");
        }
        member.setStatus(status);
        memberMapper.updateById(member);
    }
}
