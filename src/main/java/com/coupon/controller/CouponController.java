package com.coupon.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coupon.common.Result;
import com.coupon.common.annotation.ApiLimit;
import com.coupon.dto.CouponDTO;
import com.coupon.service.CouponService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 基于SpringBoot框架的个人练手项目-优惠券管理控制器
 *
 * @author JMF
 * @date 2026-04-08 12:51
 * @date 2026-04-08
 */
@Api(tags = "优惠券管理控制器")
@RestController()
@RequestMapping("/api/v1/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;

    @PostMapping()
    public Result<CouponDTO> addOrEdit(@Validated @RequestBody CouponDTO couponDTO) throws Exception {
        return Result.success(couponService.addOrEdit(couponDTO));
    }

    @DeleteMapping()
    public Result<Boolean> delete(@RequestParam Long id) {
        return Result.success(couponService.delete(id));
    }

    @GetMapping()
    public Result<CouponDTO> getCoupon(@RequestParam Long id) throws Exception {
        return Result.success(couponService.getCoupon(id));
    }

    @GetMapping("/page")
    @ApiLimit()
    public Result<IPage<CouponDTO>> pageCoupon(@Validated CouponDTO couponDTO) {
        return Result.success(couponService.pageCoupon(couponDTO));
    }

    @PostMapping("/receive")
    @ApiLimit
    public Result<CouponDTO> receiveCoupon(@RequestParam Long id) {
        return Result.success(couponService.receiveCoupon(id));
    }
}
