package com.coupon.controller;

import com.coupon.common.Result;
import com.coupon.dto.VerificationDTO;
import com.coupon.service.VerificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 核销管理控制器
 *
 * @author JMF
 * @date 2026-05-18
 */
@Api(tags = "核销管理控制器")
@RestController
@RequestMapping("/api/v1/verification")
@Validated
public class VerificationController {

    @Autowired
    private VerificationService verificationService;

    /**
     * 用户点击去核销按钮，生成核销记录
     */
    @PostMapping("/generate")
    @ApiOperation(value = "生成核销记录", notes = "用户点击去核销按钮时调用，生成一条核销记录（含随机核销码）")
    public Result<VerificationDTO> generateVerification(
            @RequestParam @NotNull(message = "优惠券模板ID不能为空") Long templateId){
        return Result.success(verificationService.generateVerification(templateId));
    }

    /**
     * 查询我的核销记录列表
     */
    @GetMapping("/my-list")
    @ApiOperation(value = "查询我的核销记录列表")
    public Result<List<VerificationDTO>> listMyVerifications() {
        return Result.success(verificationService.listMyVerifications());
    }
}