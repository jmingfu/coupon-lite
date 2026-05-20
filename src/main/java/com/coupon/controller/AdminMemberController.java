package com.coupon.controller;

import com.coupon.common.Result;
import com.coupon.dto.MemberStatusDTO;
import com.coupon.service.AdminMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 后台会员管理控制器
 *
 * @author JMF
 * @date 2026-05-20
 */
@Api(tags = "后台会员管理控制器")
@RestController
@RequestMapping("/api/v1/admin/member")
public class AdminMemberController {

    @Autowired
    private AdminMemberService adminMemberService;

    @PostMapping("/status")
    @ApiOperation(value = "更新会员状态", notes = "启用或禁用会员，status: NORMAL-正常，DISABLED-禁用")
    public Result<Boolean> updateMemberStatus(@RequestBody @Valid MemberStatusDTO statusDTO) {
        return Result.success(adminMemberService.updateMemberStatus(statusDTO));
    }
}
