package com.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coupon.common.RedisConstant;
import com.coupon.common.enums.CouponStatusEnum;
import com.coupon.common.enums.CouponTypeEnum;
import com.coupon.common.exception.ReturnException;
import com.coupon.common.util.CouponValidUtil;
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

import java.time.LocalDateTime;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
        validateCoupon(couponDTO);
        CouponTemplate coupon = new CouponTemplate();
        BeanUtils.copyProperties(couponDTO, coupon);
        coupon.setRemainCount(couponDTO.getTotalCount());
        if (couponDTO.getId() == null) {
            couponMapper.insert(coupon);
        } else {
            int rows = couponMapper.updateById(coupon);
            if (rows == 0) {
                throw new ReturnException("优惠券id不存在，更新失败");
            }
            // 删除redis缓存
            redisTemplate.delete(RedisConstant.ALL_COUPON + coupon.getId());
        }
        // 更新redis优惠券id列表
        redisTemplate.opsForSet().add(RedisConstant.COUPON_IDS, String.valueOf(coupon.getId()));
        return couponDTO;
    }

    @Override
    public Boolean delete(Long id) {
        Boolean success = couponMapper.deleteById(id) > 0;
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
         * RedisSet直接缓存已存在的优惠券id。管理端需能编辑历史数据，未入集合时仍回源数据库。
         */
        if (!isAdminUser() && Objects.equals(
                redisTemplate.opsForSet().isMember(RedisConstant.COUPON_IDS, String.valueOf(id)),
                Boolean.FALSE)) {
            throw new ReturnException("优惠券不存在");
        }
        String stringCoupon = redisTemplate.opsForValue().get(RedisConstant.ALL_COUPON + id);
        if (StringUtils.isNotBlank(stringCoupon)) {
            return objectMapper.readValue(stringCoupon, CouponDTO.class);
        }
        CouponTemplate couponTemplate = couponMapper.selectById(id);
        if (couponTemplate == null) {
            throw new ReturnException("优惠券不存在");
        }
        redisTemplate.opsForSet().add(RedisConstant.COUPON_IDS, String.valueOf(couponTemplate.getId()));
        CouponDTO couponDTO = new CouponDTO();
        BeanUtils.copyProperties(couponTemplate, couponDTO);
        redisTemplate.opsForValue().set(RedisConstant.ALL_COUPON + couponTemplate.getId(),
                objectMapper.writeValueAsString(couponDTO), 10, TimeUnit.MINUTES);
        return couponDTO;
    }

    private boolean isAdminUser() {
        try {
            MemberDTO memberInfo = MemberUtil.getMemberInfo();
            return memberInfo != null && memberInfo.getAdminId() != null;
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public IPage<CouponDTO> pageCoupon(CouponDTO dto) {
        Page<CouponTemplate> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<CouponTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(dto.getName()), CouponTemplate::getName, dto.getName());
        wrapper.eq(Objects.nonNull(dto.getType()), CouponTemplate::getType, dto.getType());

        // 判断是否为管理员：管理员可查看所有券，普通用户/未登录只看时间范围内的券
        boolean isAdmin = false;
        try {
            MemberDTO memberInfo = MemberUtil.getMemberInfo();
            if (memberInfo != null && memberInfo.getAdminId() != null) {
                isAdmin = true;
            }
        } catch (Exception ignored) {
            // 未登录用户，忽略异常
        }

        if (!isAdmin) {
            Date now = new Date();
            wrapper.le(CouponTemplate::getValidStartTime, now);
            wrapper.ge(CouponTemplate::getValidEndTime, now);
        }

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
            CouponValidUtil.checkValid(couponTemplate);
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

    private void validateCoupon(CouponDTO couponDTO) {
        CouponTypeEnum type = couponDTO.getType();
        if (type == null) {
            throw new ReturnException("请选择优惠券类型");
        }
        switch (type) {
            case FULL_REDUCTION:
                if (Objects.isNull(couponDTO.getFullAmount())) {
                    throw new ReturnException("满减券请输入满减门槛");
                }
                couponDTO.setDiscountRate(null);
                break;
            case DISCOUNT:
                if (Objects.isNull(couponDTO.getDiscountRate())) {
                    throw new ReturnException("折扣券请输入折扣率");
                }
                BigDecimal rate = couponDTO.getDiscountRate();
                if (rate.compareTo(BigDecimal.ZERO) < 0 || rate.compareTo(BigDecimal.ONE) > 0
                        || rate.scale() > 1) {
                    throw new ReturnException("折扣率须为0-1的一位小数");
                }
                couponDTO.setFullAmount(null);
                couponDTO.setDiscountAmount(null);
                break;
            case NO_THRESHOLD:
                if (Objects.isNull(couponDTO.getDiscountAmount())) {
                    throw new ReturnException("无门槛券请输入减免金额");
                }
                couponDTO.setFullAmount(null);
                couponDTO.setDiscountRate(null);
                break;
            default:
                throw new ReturnException("不支持的优惠券类型");
        }
        LocalDateTime now = truncateToMinute(LocalDateTime.now());
        LocalDateTime start = truncateToMinute(couponDTO.getValidStartTime());
        LocalDateTime end = truncateToMinute(couponDTO.getValidEndTime());
        boolean valid;
        if (couponDTO.getId() != null) {
            valid = (start == null && end == null) || (start != null && end != null && start.isBefore(end));
        } else {
            valid = start != null && end != null && !start.isBefore(now) && end.isAfter(now) && start.isBefore(end);
        }
        if (!valid) {
            throw new ReturnException("优惠券时间范围不合法，请重新设置");
        }
    }

    private LocalDateTime truncateToMinute(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.withSecond(0).withNano(0);
    }
}