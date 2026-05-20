package com.coupon.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 会员状态枚举
 * 
 * @author JMF
 * @date 2026-05-20
 */
@Getter
public enum MemberStatusEnum {

    /**
     * 正常
     */
    NORMAL(1, "正常"),

    /**
     * 禁用
     */
    DISABLED(2, "禁用");

    @EnumValue
    private final int code;

    private final String desc;

    MemberStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static MemberStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (MemberStatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }
}
