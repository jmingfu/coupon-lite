package com.coupon.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 基于SpringBoot框架的个人练手项目-crm角色枚举
 *
 * @author JMF
 * @date 2026-04-12 13:55
 * @date 2026-04-12
 */

public enum RoleEnum {
    SALES(1, "销售"),
    MANAGER(2, "主管 "),
    ADMIN(3, "管理员");
    @EnumValue
    private final int code;
    private final String desc;

    RoleEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static RoleEnum getByCode(int code) {
        for (RoleEnum type : values()) {
            if (type.code==code) {
                return type;
            }
        }
        return null;
    }
}
