package com.coupon.dto;

import com.coupon.common.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 会员登录前端实体
 *
 * @TableName member
 */
@Data
@Validated
public class MemberDTO extends PageParam implements Serializable {

    /**
     * 主键ID
     */
    @NotNull(message = "[主键ID]不能为空")
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
     * 微信openid生成的code
     */
    private String code;

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
     * 注册时间
     */
    @ApiModelProperty("注册时间")
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


    @ApiModelProperty("会员登录token")
    private String token;

    @ApiModelProperty("会员注册开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("会员注册结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("微信登录签名")
    private String sign;

    @ApiModelProperty("微信防重nonce")
    private String nonce;

    @ApiModelProperty("登录时间戳")
    private String timeStamp;
}
