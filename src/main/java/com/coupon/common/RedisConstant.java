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
    public static final String ALL_COUPON = "coupon:all:";

    // 已存在优惠券列表
    public static final String COUPON_IDS = "coupon:ids:";

    // 优惠券领取请求锁
    public static final String COUPON_RECEIVE_LOCK = "coupon:receive:lock:";

    // 优惠券一小时内访问次数
    public static final String COUPON_COUNTS = "coupon:counts:";

    //微信小程序openId
    public static final String LOGIN_OPENID = "LOGIN:token:latest:";

    //登录token
    public static final String LOGIN_TOKEN = "LOGIN_TOKEN:";
}
