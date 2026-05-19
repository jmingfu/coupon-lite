package com.coupon.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.coupon.common.enums.CouponStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户优惠券表
 *
 * @TableName member_coupon
 */
@TableName("member_coupon")
@Data
public class MemberCoupon implements Serializable {

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
    @NotNull(message = "[优惠券模板ID]不能为空")
    @ApiModelProperty("优惠券模板ID")
    private Long templateId;
    /**
     * 状态 1未使用 2待核销 3已核销 4已过期
     */
    @ApiModelProperty("状态 1未使用 2待核销 3已核销 4已过期")
    private CouponStatusEnum status;
    /**
     * 核销码（唯一）
     */
    @Size(max = 32, message = "编码长度不能超过32")
    @ApiModelProperty("核销码（唯一）")
    @Length(max = 32, message = "编码长度不能超过32")
    private String verificationCode;
    /**
     * 领取时间
     */
    @ApiModelProperty("领取时间")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date receiveTime;
    /**
     * 使用时间
     */
    @ApiModelProperty("使用时间")
    private Date useTime;


    /**
     * 券有效期开始时间
     */
    @ApiModelProperty("券有效期开始时间")
    private Date validStartTime;

    /**
     * 券有效期结束时间
     */
    @ApiModelProperty("券有效期结束时间")
    private Date validEndTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;
    /**
     * 是否删除 0否1是
     */
    @ApiModelProperty("是否删除 0否1是")
    private Integer isDeleted;


}