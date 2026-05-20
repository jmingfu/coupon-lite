package com.coupon.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coupon.dto.VerificationDTO;
import com.coupon.dto.VerificationRecordAdminDTO;
import com.coupon.dto.VerificationRecordQueryDTO;

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

    /**
     * 后台分页查询核销记录列表（使用MyBatis Plus分页插件）
     *
     * @param query 查询条件（包含分页参数）
     * @return 核销记录分页列表
     */
    IPage<VerificationRecordAdminDTO> pageVerificationRecords(VerificationRecordQueryDTO query);
}
