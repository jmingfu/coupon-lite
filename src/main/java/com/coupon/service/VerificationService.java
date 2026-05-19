package com.coupon.service;

import com.coupon.dto.VerificationDTO;

import java.util.List;

/**
 * 核销服务接口
 *
 * @author JMF
 * @date 2026-05-18
 */
public interface VerificationService {

    /**
     * 生成核销记录（用户点击去核销按钮）
     *
     * @param templateId 优惠券模板ID
     * @return 核销记录
     */
    VerificationDTO generateVerification(Long templateId);

    /**
     * 确认核销（店员扫码后点击确认）
     *
     * @param verificationCode 核销码
     * @return 核销记录
     */
    VerificationDTO confirmVerification(String verificationCode);

    /**
     * 查询会员的核销记录列表
     *
     * @return 核销记录列表
     */
    List<VerificationDTO> listMyVerifications();
}