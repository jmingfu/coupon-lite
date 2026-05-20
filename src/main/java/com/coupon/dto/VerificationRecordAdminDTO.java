package com.coupon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 后台核销记录DTO
 *
 * @author JMF
 * @date 2026-05-19
 */
@Data
public class VerificationRecordAdminDTO {

    /**
     * 核销记录ID
     */
    @ApiModelProperty("核销记录ID")
    private Long id;

    /**
     * 会员ID
     */
    @ApiModelProperty("会员ID")
    private Long memberId;

    /**
     * 用户手机号
     */
    @ApiModelProperty("用户手机号")
    private String memberPhone;

    /**
     * 优惠券模板ID
     */
    @ApiModelProperty("优惠券模板ID")
    private Long templateId;

    /**
     * 优惠券名称
     */
    @ApiModelProperty("优惠券名称")
    private String couponName;

    /**
     * 核销码
     */
    @ApiModelProperty("核销码")
    private String verificationCode;

    /**
     * 核销人（店员/收银员）
     */
    @ApiModelProperty("核销人（店员/收银员）")
    private String verifier;

    /**
     * 核销时间
     */
    @ApiModelProperty("核销时间")
    private LocalDateTime verificationTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
