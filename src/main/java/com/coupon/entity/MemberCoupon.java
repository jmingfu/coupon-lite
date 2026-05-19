package com.coupon.entity;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.coupon.common.enums.CouponStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
* 用户优惠券表
* @TableName member_coupon
*/
@TableName("member_coupon")
@Data
public class MemberCoupon implements Serializable {

    /**
    * 主键ID
    */
    @NotNull(message="[主键ID]不能为空")
    @ApiModelProperty("主键ID")
    private Long id;
    /**
    * 会员ID
    */
    @NotNull(message="[会员ID]不能为空")
    @ApiModelProperty("会员ID")
    private Long memberId;
    /**
    * 优惠券模板ID
    */
    @NotNull(message="[优惠券模板ID]不能为空")
    @ApiModelProperty("优惠券模板ID")
    private Long templateId;
    /**
    * 状态 1已领取 2待核销 3已核销 4已过期
    */
    @ApiModelProperty("状态 1已领取 2待核销 3已核销 4已过期")
    private CouponStatusEnum status;
    /**
    * 核销码（唯一）
    */
    @Size(max= 32,message="编码长度不能超过32")
    @ApiModelProperty("核销码（唯一）")
    @Length(max= 32,message="编码长度不能超过32")
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