package com.coupon.common;

/**
 * 基于SpringBoot框架的个人练手项目-redis常量
 *
 * @author JMF
 * @date 2026-04-08 15:44
 * @date 2026-04-08
 */
public class RedisConstant {
    // 所有优惠券
    public static final String ALL_COUPON = "COUPON:all:";

    // 已存在优惠券列表
    public static final String COUPON_IDS = "COUPON:ids:";

    //微信小程序openId/会员登录标识
    public static final String LOGIN_OPENID = "LOGIN:token:latest:";

    //登录token
    public static final String LOGIN_TOKEN = "LOGIN_TOKEN:";

    //登录防重字符串
    public static final String LOGIN_NONCE = "LOGIN_NONCE:";

    //接口限流key
    public static final String LIMIT_URL = "LIMIT_url:";

    //openid限流key
    public static final String LIMIT_openid = "LIMIT_openid:";
}
