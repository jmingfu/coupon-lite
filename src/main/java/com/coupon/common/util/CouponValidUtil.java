package com.coupon.common.util;

import com.coupon.common.exception.ReturnException;
import com.coupon.entity.CouponTemplate;

import java.util.Date;

public class CouponValidUtil {

    public static void checkValid(CouponTemplate couponTemplate) {
        Date now = new Date();
        if (now.before(couponTemplate.getValidStartTime())) {
            throw new ReturnException("优惠券尚未生效");
        }
        if (now.after(couponTemplate.getValidEndTime())) {
            throw new ReturnException("优惠券已过期");
        }
    }
}
