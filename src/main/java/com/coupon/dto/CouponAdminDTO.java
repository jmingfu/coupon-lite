package com.coupon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 后台管理员DTO
 *
 * @author JMF
 * @date 2026-05-19
 */
@Data
public class CouponAdminDTO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("用户名（必填，只能包含字母数字下划线4-20位）")
    private String username;

    @ApiModelProperty("手机号（可选）")
    private String phone;

    @ApiModelProperty("密码（6-20位）")
    private String password;

    @ApiModelProperty("登录token")
    private String token;
}
