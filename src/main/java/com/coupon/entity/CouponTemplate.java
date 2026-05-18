package com.coupon.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.coupon.common.enums.CouponTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
* 优惠券模板表
* @TableName coupon_template
*/
@TableName("coupon_template")
@Data
public class CouponTemplate implements Serializable {

    /**
    * 主键ID
    */
    @NotNull(message="[主键ID]不能为空")
    @ApiModelProperty("主键ID")
    private Long id;
    /**
    * 优惠券名称
    */
    @NotBlank(message="[优惠券名称]不能为空")
    @Size(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("优惠券名称")
    @Length(max= 64,message="编码长度不能超过64")
    private String name;
    /**
    * 类型 1满减 2折扣 3无门槛
    */
    @NotNull(message="[类型 1满减 2折扣 3无门槛]不能为空")
    @ApiModelProperty("类型 1满减 2折扣 3无门槛")
    private CouponTypeEnum type;
    /**
    * 满减门槛（满减券必填）
    */
    @ApiModelProperty("满减门槛（满减券必填）")
    private BigDecimal fullAmount;
    /**
    * 减免金额（满减/无门槛）
    */
    @ApiModelProperty("减免金额（满减/无门槛）")
    private BigDecimal discountAmount;
    /**
    * 折扣率（折扣券，如0.8表示8折）
    */
    @ApiModelProperty("折扣率（折扣券，如0.8表示8折）")
    private BigDecimal discountRate;
    /**
    * 发放总量
    */
    @NotNull(message="[发放总量]不能为空")
    @ApiModelProperty("发放总量")
    private Integer totalCount;
    /**
    * 剩余库存
    */
    @NotNull(message="[剩余库存]不能为空")
    @ApiModelProperty("剩余库存")
    private Integer remainCount;
    /**
    * 有效期开始
    */
    @NotNull(message="[有效期开始]不能为空")
    @ApiModelProperty("有效期开始")
    private Date validStartTime;
    /**
    * 有效期结束
    */
    @NotNull(message="[有效期结束]不能为空")
    @ApiModelProperty("有效期结束")
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
