package com.coupon.common.util;

import com.coupon.common.exception.ReturnException;
import com.coupon.entity.CouponTemplate;

import java.time.LocalDateTime;
import java.util.Date;

public class CouponValidUtil {

    public static void checkValid(CouponTemplate couponTemplate) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(couponTemplate.getValidStartTime())) {
            throw new ReturnException("优惠券尚未生效");
        }
        if (now.isAfter(couponTemplate.getValidEndTime())) {
            throw new ReturnException("优惠券已过期");
        }
    }
}
