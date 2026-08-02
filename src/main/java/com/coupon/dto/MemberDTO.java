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
 */
@Data
@Validated
public class MemberDTO extends PageParam implements Serializable {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("微信openid（模拟授权）")
    private String openid;

    @ApiModelProperty("前端生成，用于换取openid")
    private String code;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("注册时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("是否删除 0否1是")
    private Integer isDeleted;

    @ApiModelProperty("会员登录token")
    private String token;

    @ApiModelProperty("管理员ID，如果是管理员就设置")
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
