package com.coupon.controller;

import com.coupon.common.Result;
import com.coupon.dto.CouponAdminDTO;
import com.coupon.service.CouponAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 后台管理员控制器
 *
 * @author JMF
 * @date 2026-05-19
 */
@Api(tags = "后台管理员控制器")
@RestController
@RequestMapping("/api/v1/admin")
@Validated
public class CouponAdminController {

    @Autowired
    private CouponAdminService couponAdminService;

    /**
     * 管理员注册
     * 前端调用示例：
     * POST /api/v1/admin/register
     * {
     *   "phone": "13800138000",
     *   "password": "123456"
     * }
     */
    @PostMapping("/register")
    @ApiOperation(value = "管理员注册", notes = "使用用户名和密码注册管理员账号")
    public Result<CouponAdminDTO> register(@RequestBody @Valid CouponAdminDTO dto) {
        return Result.success(couponAdminService.register(dto));
    }

    /**
     * 管理员登录
     * 前端调用示例：
     * POST /api/v1/admin/login
     * {
     *   "phone": "13800138000",
     *   "password": "123456"
     * }
     */
    @PostMapping("/login")
    @ApiOperation(value = "管理员登录", notes = "使用手机号和密码登录")
    public Result<CouponAdminDTO> login(@RequestBody @Valid CouponAdminDTO dto) {
        return Result.success(couponAdminService.login(dto));
    }
}
