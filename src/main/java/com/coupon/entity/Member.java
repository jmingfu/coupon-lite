package com.coupon.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.coupon.common.enums.MemberStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员表
 *
 * @TableName member
 */
@TableName("member")
@Data
public class Member implements Serializable {

    /**
     * 主键ID
     */
    @NotNull(message = "[主键ID]不能为空")
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;
    /**
     * 微信openid（模拟授权）
     */
    @NotBlank(message = "[微信openid（模拟授权）]不能为空")
    @Size(max = 64, message = "编码长度不能超过64")
    @ApiModelProperty("微信openid（模拟授权）")
    @Length(max = 64, message = "编码长度不能超过64")
    private String openid;
    /**
     * 昵称
     */
    @Size(max = 32, message = "编码长度不能超过32")
    @ApiModelProperty("昵称")
    @Length(max = 32, message = "编码长度不能超过32")
    private String nickname;
    /**
     * 手机号
     */
    @Size(max = 16, message = "编码长度不能超过16")
    @ApiModelProperty("手机号")
    @Length(max = 16, message = "编码长度不能超过16")
    private String phone;
    /**
     * 储值余额（可选，先不做）
     */
    @ApiModelProperty("储值余额（可选，先不做）")
    private BigDecimal balance;
    /**
     * 积分（可选，先不做）
     */
    @ApiModelProperty("积分（可选，先不做）")
    private Integer points;
    /**
     * 会员状态 1正常 2禁用
     */
    @ApiModelProperty("会员状态")
    private MemberStatusEnum status;
    /**
     * 注册时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("注册时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("更新时间")
    private Date updateTime;
    /**
     * 是否删除 0否1是，逻辑删除字段
     */
    @TableLogic
    @ApiModelProperty("是否删除 0否1是")
    private Integer isDeleted;


}
