package com.coupon.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coupon.common.Result;
import com.coupon.dto.VerificationDTO;
import com.coupon.dto.VerificationRecordAdminDTO;
import com.coupon.dto.VerificationRecordQueryDTO;
import com.coupon.service.VerificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
            @RequestParam @NotNull(message = "优惠券模板ID不能为空") Long templateId) {
        return Result.success(verificationService.generateVerification(templateId));
    }

    /**
     * 店员扫码后确认核销
     */
    @PostMapping("/confirm")
    @ApiOperation(value = "确认核销", notes = "店员扫码后点击确认核销，将优惠券状态改为已核销")
    public Result<VerificationDTO> confirmVerification(
            @RequestParam @NotBlank(message = "核销码不能为空") String verificationCode) {
        return Result.success(verificationService.confirmVerification(verificationCode));
    }

    /**
     * 查询我的核销记录列表
     */
    @GetMapping("/my-list")
    @ApiOperation(value = "查询我的核销记录列表")
    public Result<List<VerificationDTO>> listMyVerifications() {
        return Result.success(verificationService.listMyVerifications());
    }

    /**
     * 后台分页查询核销记录列表（使用MyBatis Plus分页插件）
     * 前端调用示例：
     * POST /api/v1/verification/admin/page
     * {
     * "pageNum": 1,
     * "pageSize": 10,
     * "phone": "13800138000",
     * "startTime": "2026-05-01 00:00:00",
     * "endTime": "2026-05-31 23:59:59"
     * }
     */
    @PostMapping("/admin/page")
    @ApiOperation(value = "后台查询核销记录", notes = "后台管理使用，支持分页和条件查询，参数以JSON对象形式传递")
    public Result<IPage<VerificationRecordAdminDTO>> pageVerificationRecords(
            @ApiParam("查询条件") @RequestBody @Valid VerificationRecordQueryDTO query) {

        // 设置默认分页参数
        if (query.getPageNum() == null || query.getPageNum() <= 0) {
            query.setPageNum(1);
        }
        if (query.getPageSize() == null || query.getPageSize() <= 0) {
            query.setPageSize(10);
        }

        Page<VerificationRecordAdminDTO> page = new Page<>(query.getPageNum(), query.getPageSize());
        return Result.success(verificationService.pageVerificationRecords(page, query));
    }


}
