package com.coupon.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coupon.dto.CouponDTO;

import java.util.List;

/**
 * 基于SpringBoot框架的个人练手项目-
 *
 * @author JMF
 * @date 2026-04-08 13:33
 * @date 2026-04-08
 */
public interface CouponService {
    //新增/编辑优惠券模板
    CouponDTO addOrEdit(CouponDTO couponDTO) throws Exception;

    //删除优惠券
    Boolean delete(Long id);

    //查看单个优惠券
    CouponDTO getCoupon(Long id) throws Exception;

    //
    IPage<CouponDTO> pageCoupon(CouponDTO dto);

    //领取优惠券
    CouponDTO receiveCoupon(Long couponId);

    List<CouponDTO> getMyCoupons();

//    List<CouponDTO> generateCode(Long couponId);
}
