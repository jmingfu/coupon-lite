package com.coupon.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 基于SpringBoot框架的个人练手项目-微信小程序接口返回结果
 *
 * @author JMF
 * @date 2026-04-05 16:47
 * @date 2026-04-05
 */
@Data
@Api(tags = "微信小程序接口返回结果")
public class WechatCode2SessionRes {
    @ApiModelProperty("用户唯一标识")
    private String openid;

    @ApiModelProperty("会话密钥")
    private String SessionKey;

    @ApiModelProperty("用户在开放平台的唯一标识符（若绑定）")
    private String unionId;

    @ApiModelProperty("错误码 0=成功")
    private String errCode;

    @ApiModelProperty("错误信息")
    private String errMsg;
}
