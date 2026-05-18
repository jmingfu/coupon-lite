package com.coupon.dto;

import io.swagger.annotations.Api;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 基于SpringBoot框架的个人练手项目-
 *
 * @author JMF
 * @date 2026-04-07 10:06
 * @date 2026-04-07
 */
@Api(tags = "微信签名，防止重刷导致微信平台风控")
@Data
public class WxSign {
    @NotBlank(message = "timestamp不能为空")
    private String timestamp;

    @NotBlank(message = "nonce不能为空")
    private String nonce;

    @NotBlank(message = "sign不能为空")
    private String sign;
}
