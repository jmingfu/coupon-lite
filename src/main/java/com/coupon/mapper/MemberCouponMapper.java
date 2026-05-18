package com.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coupon.entity.MemberCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 基于SpringBoot框架的个人练手项目-会员优惠券mapper
 *
 * @author JMF
 * @date 2026-04-11 12:22
 * @date 2026-04-11
 */
@Repository
@Mapper
public interface MemberCouponMapper extends BaseMapper<MemberCoupon> {

}
