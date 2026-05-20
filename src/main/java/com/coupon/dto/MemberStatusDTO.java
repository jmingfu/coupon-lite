package com.coupon.dto;

import com.coupon.common.enums.MemberStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 会员状态更新DTO
 *
 * @author JMF
 * @date 2026-05-20
 */
@Data
@ApiModel("会员状态更新请求")
public class MemberStatusDTO {

    @NotNull(message = "会员ID不能为空")
    @ApiModelProperty(value = "会员ID", required = true)
    private Long memberId;

    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "会员状态：NORMAL-正常，DISABLED-禁用", required = true)
    private MemberStatusEnum status;
}
