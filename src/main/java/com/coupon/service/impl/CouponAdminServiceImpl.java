package com.coupon.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.coupon.common.RedisConstant;
import com.coupon.common.exception.ReturnException;
import com.coupon.dto.CouponAdminDTO;
import com.coupon.dto.MemberDTO;
import com.coupon.entity.CouponAdmin;
import com.coupon.mapper.CouponAdminMapper;
import com.coupon.service.CouponAdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 后台管理员服务实现类
 *
 * @author JMF
 * @date 2026-05-19
 */
@Service
public class CouponAdminServiceImpl implements CouponAdminService {

    @Autowired
    private CouponAdminMapper couponAdminMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * token过期时间（7天）
     */
    private static final long TOKEN_EXPIRE_DAYS = 7;

    /**
     * 用户名校验正则：只能包含字母数字下划线4-20位
     */
    private static final String USERNAME_REGEX = "^\\w{4,20}$";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CouponAdminDTO register(CouponAdminDTO dto) {
        // 校验用户名
        if (StringUtils.isBlank(dto.getUsername())) {
            throw new ReturnException("用户名不能为空");
        }
        if (!dto.getUsername().matches(USERNAME_REGEX)) {
            throw new ReturnException("用户名只能包含字母数字下划线，长度4-20位");
        }

        // 校验密码
        if (StringUtils.isBlank(dto.getPassword())) {
            throw new ReturnException("密码不能为空");
        }
        if (dto.getPassword().length() < 6 || dto.getPassword().length() > 20) {
            throw new ReturnException("密码长度必须为6-20位");
        }

        // 检查用户名是否已注册
        LambdaQueryWrapper<CouponAdmin> usernameWrapper = new LambdaQueryWrapper<>();
        usernameWrapper.eq(CouponAdmin::getUsername, dto.getUsername());
        CouponAdmin existAdmin = couponAdminMapper.selectOne(usernameWrapper);
        if (existAdmin != null) {
            throw new ReturnException("该用户名已注册");
        }

        // 如果填写了手机号，检查是否已注册
        if (StringUtils.isNotBlank(dto.getPhone())) {
            LambdaQueryWrapper<CouponAdmin> phoneWrapper = new LambdaQueryWrapper<>();
            phoneWrapper.eq(CouponAdmin::getPhone, dto.getPhone());
            CouponAdmin existPhoneAdmin = couponAdminMapper.selectOne(phoneWrapper);
            if (existPhoneAdmin != null) {
                throw new ReturnException("该手机号已注册");
            }
        }

        // 创建管理员
        CouponAdmin admin = new CouponAdmin();
        admin.setUsername(dto.getUsername());
        admin.setPhone(dto.getPhone());
        // 密码MD5加密
        admin.setPassword(SecureUtil.md5(dto.getPassword()));

        couponAdminMapper.insert(admin);

        // 生成token
        String token = UUID.randomUUID().toString().replace("-", "");

        // 存入Redis（使用MemberDTO统一格式）
        try {
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setAdminId(admin.getId()); // 管理员ID存入adminId字段
            memberDTO.setNickname(dto.getUsername()); // 用户名存入nickname
            memberDTO.setPhone(admin.getPhone());
            memberDTO.setToken(token);

            // 按username存token（与会员登录一致）
            stringRedisTemplate.opsForValue().set(
                    RedisConstant.LOGIN_OPENID + dto.getUsername(),
                    token,
                    TOKEN_EXPIRE_DAYS,
                    TimeUnit.DAYS
            );
            // 按token存用户信息（与会员登录一致，统一使用MemberDTO）
            stringRedisTemplate.opsForValue().set(
                    RedisConstant.LOGIN_TOKEN + token,
                    objectMapper.writeValueAsString(memberDTO),
                    TOKEN_EXPIRE_DAYS,
                    TimeUnit.DAYS
            );

            // 设置返回值
            dto.setId(admin.getId());
            dto.setPassword(null); // 不返回密码
            dto.setToken(token);
        } catch (Exception e) {
            throw new ReturnException("注册失败");
        }

        return dto;
    }

    @Override
    public CouponAdminDTO login(CouponAdminDTO dto) {
        // 校验用户名
        if (StringUtils.isBlank(dto.getUsername())) {
            throw new ReturnException("请输入用户名");
        }
        // 校验密码
        if (StringUtils.isBlank(dto.getPassword())) {
            throw new ReturnException("密码不能为空");
        }

        // 查询管理员
        LambdaQueryWrapper<CouponAdmin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CouponAdmin::getUsername, dto.getUsername());
        CouponAdmin admin = couponAdminMapper.selectOne(wrapper);

        if (admin == null) {
            throw new ReturnException("用户不存在");
        }

        // 校验密码
        if (!SecureUtil.md5(dto.getPassword()).equals(admin.getPassword())) {
            throw new ReturnException("密码错误");
        }

        // 生成新token
        String token = UUID.randomUUID().toString().replace("-", "");

        // 单点登录：删除旧token（与会员登录一致）
        String oldToken = stringRedisTemplate.opsForValue().get(RedisConstant.LOGIN_OPENID + dto.getUsername());
        if (StringUtils.isNotBlank(oldToken)) {
            stringRedisTemplate.delete(RedisConstant.LOGIN_TOKEN + oldToken);
        }

        // 存入Redis（使用MemberDTO统一格式）
        try {
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setAdminId(admin.getId()); // 管理员ID存入adminId字段
            memberDTO.setNickname(admin.getUsername()); // 用户名存入nickname
            memberDTO.setPhone(admin.getPhone());
            memberDTO.setToken(token);

            // 按username存token（与会员登录一致）
            stringRedisTemplate.opsForValue().set(
                    RedisConstant.LOGIN_OPENID + dto.getUsername(),
                    token,
                    TOKEN_EXPIRE_DAYS,
                    TimeUnit.DAYS
            );
            // 按token存用户信息（与会员登录一致，统一使用MemberDTO）
            stringRedisTemplate.opsForValue().set(
                    RedisConstant.LOGIN_TOKEN + token,
                    objectMapper.writeValueAsString(memberDTO),
                    TOKEN_EXPIRE_DAYS,
                    TimeUnit.DAYS
            );

            // 设置返回值
            dto.setId(admin.getId());
            dto.setPassword(null); // 不返回密码
            dto.setToken(token);
        } catch (Exception e) {
            throw new ReturnException("登录失败");
        }

        return dto;
    }
}
