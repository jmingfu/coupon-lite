package com.coupon.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 优惠券状态枚举
 * 
 * @author JMF
 * @date 2026-05-19
 */
@Getter
public enum CouponStatusEnum {

    /**
     * 已领取
     */
    RECEIVED(1, "已领取"),

    /**
     * 待核销
     */
    PENDING_VERIFICATION(2, "待核销"),

    /**
     * 已核销
     */
    VERIFIED(3, "已核销"),

    /**
     * 已过期
     */
    EXPIRED(4, "已过期");

    @EnumValue
    private final int code;

    private final String desc;

    CouponStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static CouponStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (CouponStatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }
}