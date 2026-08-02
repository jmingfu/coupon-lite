package com.coupon.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coupon.common.Result;
import com.coupon.common.annotation.ApiLimit;
import com.coupon.dto.CouponDTO;
import com.coupon.dto.MemberDTO;
import com.coupon.service.CouponService;
import com.coupon.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 基于SpringBoot框架的个人练手项目-会员管理控制器
 *
 * @author JMF
 * @date 2026-04-05 15:41
 * @date 2026-04-05
 */
@RestController
@RequestMapping("/api/v1/member")
@Api(tags = "会员基础管理")
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private CouponService couponService;

    @PostMapping("/login-or-register")
    @ApiLimit
    @ApiOperation(value = "注册并登录")
    public Result<MemberDTO> wxLogin(@RequestBody MemberDTO memberDTO) throws Exception {
        return Result.success(memberService.wxLogin(memberDTO));
    }

    @GetMapping("")
    @ApiLimit
    @ApiOperation(value = "根据id查询会员")
    public Result<MemberDTO> getById(@RequestParam Long id) {
        return Result.success(memberService.getById(id));
    }

    @GetMapping("/page")
    @ApiLimit
    @ApiOperation(value = "分页条件查询会员列表")
    public Result<IPage<MemberDTO>> selectPage(@Validated MemberDTO memberDTO) {
        return Result.success(memberService.selectPage(memberDTO));
    }

    @GetMapping("/my-coupon")
    @ApiLimit()
    @ApiOperation(value = "查看我的优惠券")
    public Result<List<CouponDTO>> myCoupon() {
        return Result.success(couponService.getMyCoupons());
    }
}
