package com.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 核销记录表
 *
 * @TableName verification_record
 */
@TableName("verification_record")
@Data
public class VerificationRecord {

    /**
     * 主键ID
     */
    @NotNull(message = "[主键ID]不能为空")
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;


    /**
     * 会员ID
     */
    @NotNull(message = "[会员ID]不能为空")
    @ApiModelProperty("会员ID")
    private Long memberId;

    /**
     * 优惠券模板ID
     */
    @NotNull(message = "[模板ID]不能为空")
    @ApiModelProperty("模板ID")
    private Long templateId;

    /**
     * 核销码
     */
    @NotBlank(message = "[核销码]不能为空")
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
    private Date verificationTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField(updateStrategy = com.baomidou.mybatisplus.annotation.FieldStrategy.NEVER)
    private Date createTime;
}