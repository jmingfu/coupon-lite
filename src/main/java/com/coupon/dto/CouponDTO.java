package com.coupon.dto;

import com.coupon.common.PageParam;
import com.coupon.common.enums.CouponTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 基于SpringBoot框架的个人练手项目-优惠券模板，前后端参数
 *
 * @author JMF
 * @LocalDateTime 2026-04-08 12:57
 * @LocalDateTime 2026-04-08
 */
@Data
@Api(tags = "优惠券模板，前后端参数")
@Validated
public class CouponDTO extends PageParam {

    @ApiModelProperty("主键ID")
    private Long id;
    /**
     * 优惠券名称
     */

    @ApiModelProperty("优惠券名称")
    private String name;
    /**
     * 类型 1满减 2折扣 3无门槛
     */
    @ApiModelProperty("类型 1满减 2折扣 3无门槛")
    private CouponTypeEnum type;
    /**
     * 满减门槛（满减券必填）
     */
    @ApiModelProperty("满减门槛（满减券必填）")
    @DecimalMin(value = "0.01", message = "满减门槛必须大于0")
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
    @ApiModelProperty("发放总量")
    private Integer totalCount;
    /**
     * 剩余库存
     */
    @ApiModelProperty("剩余库存")
    private Integer remainCount;
    /**
     * 有效期开始
     */
    @ApiModelProperty("有效期开始")
    private LocalDateTime validStartTime;
    /**
     * 有效期结束
     */
    @ApiModelProperty("有效期结束")
    private LocalDateTime validEndTime;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime upLocalDateTimeTime;
    /**
     * 是否删除 0否1是
     */
    @ApiModelProperty("是否删除 0否1是")
    private Integer isDeleted;

    @ApiModelProperty("是否过期 0否1是")
    private Integer isExpire;

    /**
     * 领取时间
     */
    @ApiModelProperty("领取时间")
    private LocalDateTime receiveTime;

    /**
     * 使用时间
     */
    @ApiModelProperty("使用时间")
    private LocalDateTime useTime;

    /**
     * 状态 1未使用 2已使用 3已过期
     */
    @ApiModelProperty("状态 1未使用 2已使用 3已过期")
    private Integer status;

    /**
     * 核销码（唯一）
     */
    @ApiModelProperty("核销码（唯一）")
    private String verificationCode;
}
