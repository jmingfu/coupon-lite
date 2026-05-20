package com.coupon.service;

import com.coupon.dto.CouponAdminDTO;

/**
 * 后台管理员服务接口
 *
 * @author JMF
 * @date 2026-05-19
 */
public interface CouponAdminService {

    /**
     * 管理员注册
     *
     * @param dto 管理员信息
     * @return 管理员信息（含token）
     */
    CouponAdminDTO register(CouponAdminDTO dto);

    /**
     * 管理员登录
     *
     * @param dto 登录信息（phone、password）
     * @return 管理员信息（含token）
     */
    CouponAdminDTO login(CouponAdminDTO dto);
}
