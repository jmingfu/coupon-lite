package com.coupon.dto;

import com.coupon.common.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 核销记录查询参数DTO
 *
 * @author JMF
 * @date 2026-05-19
 */
@Data
public class VerificationRecordQueryDTO extends PageParam {


    /**
     * 用户手机号（模糊查询）
     */
    @ApiModelProperty("用户手机号（模糊查询）")
    private String phone;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
