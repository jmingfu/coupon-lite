package com.coupon.service.impl;

import com.coupon.common.enums.MemberStatusEnum;
import com.coupon.common.exception.ReturnException;
import com.coupon.common.util.MemberUtil;
import com.coupon.dto.MemberDTO;
import com.coupon.dto.MemberStatusDTO;
import com.coupon.entity.Member;
import com.coupon.mapper.MemberMapper;
import com.coupon.service.AdminMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 后台会员管理服务实现类
 *
 * @author JMF
 * @date 2026-05-20
 */
@Service
public class AdminMemberServiceImpl implements AdminMemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateMemberStatus(MemberStatusDTO statusDTO) {
        // 验证当前用户是否为管理员
        MemberDTO operator = MemberUtil.getMemberInfo();
        if (operator.getAdminId() == null) {
            throw new ReturnException("权限不足，只有管理员可以操作");
        }

        // 查询会员
        Member member = memberMapper.selectById(statusDTO.getMemberId());
        if (member == null) {
            throw new ReturnException("会员不存在");
        }

        // 检查是否重复操作
        MemberStatusEnum targetStatus = statusDTO.getStatus();
        if (member.getStatus() == targetStatus) {
            throw new ReturnException("会员已处于" + targetStatus.getDesc() + "状态");
        }

        // 更新会员状态
        member.setStatus(targetStatus);
        memberMapper.updateById(member);

        return true;
    }
}
