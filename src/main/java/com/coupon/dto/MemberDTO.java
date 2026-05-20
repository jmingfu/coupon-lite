package com.coupon.dto;

import com.coupon.common.PageParam;
import com.coupon.common.enums.MemberStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

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
    @ApiModelProperty("主键ID")
    private Long id;
    /**
     * 微信openid（模拟授权）
     */
    @ApiModelProperty("微信openid（模拟授权）")
    private String openid;

    /**
     * 微信openid生成的code
     */
    private String code;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickname;
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
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

    /**
     * 管理员ID（管理员登录时使用，用于区分管理员和会员）
     */
    @ApiModelProperty("管理员ID")
    private Long adminId;

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

    /**
     * 会员状态 1正常 2禁用
     */
    @ApiModelProperty("会员状态")
    private MemberStatusEnum status;

}
