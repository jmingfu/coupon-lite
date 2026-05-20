package com.coupon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 核销记录DTO
 *
 * @author JMF
 * @date 2026-05-18
 */
@Data
@Validated
public class VerificationDTO {

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    private Long id;

    /**
     * 会员ID
     */
    @ApiModelProperty("会员ID")
    private Long memberId;

    /**
     * 模板ID
     */
    @ApiModelProperty("模板ID")
    private Long templateId;

    /**
     * 核销码
     */
    @Size(max = 32, message = "编码长度不能超过32")
    @ApiModelProperty("核销码")
    private String verificationCode;

    /**
     * 核销人（模拟收银员）
     */
    @Size(max = 32, message = "编码长度不能超过32")
    @ApiModelProperty("核销人（模拟收银员）")
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

    /**
     * 优惠券名称
     */
    @ApiModelProperty("优惠券名称")
    private String couponName;

    /**
     * 优惠券类型
     */
    @ApiModelProperty("优惠券类型")
    private Integer couponType;
}