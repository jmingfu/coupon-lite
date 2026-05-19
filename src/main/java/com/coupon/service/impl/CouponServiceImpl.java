package com.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coupon.common.RedisConstant;
import com.coupon.common.enums.CouponStatusEnum;
import com.coupon.common.enums.CouponTypeEnum;
import com.coupon.common.exception.ReturnException;
import com.coupon.common.util.MemberUtil;
import com.coupon.dto.CouponDTO;
import com.coupon.dto.MemberDTO;
import com.coupon.entity.CouponTemplate;
import com.coupon.entity.MemberCoupon;
import com.coupon.mapper.CouponMapper;
import com.coupon.mapper.MemberCouponMapper;
import com.coupon.service.CouponService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 基于SpringBoot框架的个人练手项目-
 *
 * @author JMF
 * @date 2026-04-08 13:34
 * @date 2026-04-08
 */
@Service
public class CouponServiceImpl implements CouponService {
    @Autowired
    CouponMapper couponMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MemberCouponMapper memberCouponMapper;

    @Override
    public CouponDTO addOrEdit(CouponDTO couponDTO) throws Exception {
        CouponTemplate coupon = new CouponTemplate();
        BeanUtils.copyProperties(couponDTO, coupon);
        if (couponDTO.getType().equals(CouponTypeEnum.FULL_REDUCTION) && (Objects.isNull(couponDTO.getFullAmount()))) {
            throw new ReturnException("请输入满减门槛");
        }
        if (couponDTO.getId() == null) {
            couponMapper.insert(coupon);
        } else {
            int rows = couponMapper.updateById(coupon);

            if (rows == 0) {
                throw new ReturnException("优惠券id不存在，更新失败");
            }
            // 更新redis
            redisTemplate.opsForValue().set(RedisConstant.ALL_COUPON + coupon.getId(),
                    objectMapper.writeValueAsString(couponDTO));
        }
        // 更新redis优惠券id列表
        redisTemplate.opsForSet().add(RedisConstant.COUPON_IDS, String.valueOf(coupon.getId()));
        return couponDTO;
    }

    @Override
    public Boolean delete(Long id) {
        Boolean success = couponMapper.deleteById(id) == 0;
        // 清除缓存
        if (success) {
            redisTemplate.opsForSet().remove(RedisConstant.COUPON_IDS, String.valueOf(id));
            redisTemplate.delete(RedisConstant.ALL_COUPON + id);
        }
        return success;
    }

    @Override
    public CouponDTO getCoupon(Long id) throws Exception {
        /*
         * 先做防穿透处理，由于线下门店，客户到店使用、查看优惠券对于一致性要求高，使用空值缓存会产生临界问题：当优惠券缓存过期前很短时间内，产生
         * 了一个空值缓存，这时就会导致已有的优惠券，但是小程序端提示优惠券不存在；而数据量少的情况下，不需要用布隆过滤器，因为维护麻烦.因此这里我使用
         * RedisSet直接缓存已存在的优惠券id
         */
        if (Objects.equals(redisTemplate.opsForSet().isMember(RedisConstant.COUPON_IDS, String.valueOf(id)),
                Boolean.FALSE)) {
            throw new ReturnException("优惠券不存在");
        }
        String stringCoupon = redisTemplate.opsForValue().get(RedisConstant.ALL_COUPON);
        if (StringUtils.isNotBlank(stringCoupon)) {
            return objectMapper.readValue(stringCoupon, CouponDTO.class);
        }
        CouponTemplate couponTemplate = couponMapper.selectById(id);
        CouponDTO couponDTO = new CouponDTO();
        BeanUtils.copyProperties(couponTemplate, couponDTO);
        // 存入redis
        redisTemplate.opsForValue().set(RedisConstant.ALL_COUPON, objectMapper.writeValueAsString(couponDTO));
        return couponDTO;
    }

    @Override
    public IPage<CouponDTO> pageCoupon(CouponDTO dto) {
        Page<CouponTemplate> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<CouponTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(dto.getName()), CouponTemplate::getName, dto.getName());
        wrapper.eq(Objects.nonNull(dto.getType()), CouponTemplate::getType, dto.getType());
        wrapper.ge(Objects.nonNull(dto.getValidStartTime()) && Objects.nonNull(dto.getValidEndTime()),
                CouponTemplate::getValidStartTime, dto.getValidStartTime());
        wrapper.le(Objects.nonNull(dto.getValidStartTime()) && Objects.nonNull(dto.getValidEndTime()),
                CouponTemplate::getValidEndTime, dto.getValidEndTime());
        wrapper.gt(Objects.nonNull(dto.getIsExpire()) && dto.getIsExpire() == 1,
                CouponTemplate::getValidEndTime, new Date());
        Page<CouponTemplate> templatePage = couponMapper.selectPage(page, wrapper);
        return templatePage.convert(coupon -> {
            CouponDTO couponDTO = new CouponDTO();
            BeanUtils.copyProperties(coupon, couponDTO);
            return couponDTO;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CouponDTO receiveCoupon(Long couponId) {
        MemberDTO memberInfo = MemberUtil.getMemberInfo();
        CouponDTO couponDTO = new CouponDTO();
        try {
            // 先判断是否重复领取
            LambdaQueryWrapper<MemberCoupon> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MemberCoupon::getMemberId, memberInfo.getId());
            wrapper.eq(MemberCoupon::getTemplateId, couponId);
            if (memberCouponMapper.exists(wrapper)) {
                throw new ReturnException("请勿重复领取");
            }
            // 数据库直接原子扣减
            if (couponMapper.decreaseAmount(couponId) == 0) {
                throw new ReturnException("优惠券已领完");
            }
            CouponTemplate couponTemplate = couponMapper.selectById(couponId);
            MemberCoupon memberCoupon = new MemberCoupon();
            memberCoupon.setMemberId(memberInfo.getId());
            memberCoupon.setTemplateId(couponId);
            memberCoupon.setStatus(CouponStatusEnum.UNUSED); // 设置状态为未使用
            memberCoupon.setValidStartTime(couponTemplate.getValidStartTime());
            memberCoupon.setValidEndTime(couponTemplate.getValidEndTime());
            memberCouponMapper.insert(memberCoupon);
            BeanUtils.copyProperties(couponTemplate, couponDTO);
        } catch (DuplicateKeyException duplicateKeyException) {
            throw new ReturnException("请勿重复领取");
        }
        return couponDTO;
    }

    @Override
    public List<CouponDTO> getMyCoupons() {
        MemberDTO memberInfo = MemberUtil.getMemberInfo();
        return couponMapper.getMyCoupons(memberInfo.getId());
    }

    // @Override
    // public List<CouponDTO> generateCode(Long couponId) {
    //
    // }
}