package com.coupon.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coupon.common.enums.MemberStatusEnum;
import com.coupon.dto.MemberDTO;

/**
 * 基于SpringBoot框架的个人练手项目-
 *
 * @author JMF
 * @date 2026-04-05 16:22
 * @date 2026-04-05
 */

public interface MemberService {
    MemberDTO wxLogin(MemberDTO memberDTO) throws Exception;

    MemberDTO getById(Long id);

    IPage<MemberDTO> selectPage(MemberDTO memberDTO);

    /**
     * 更新会员状态（禁用/启用）
     * @param memberId 会员ID
     * @param status 目标状态
     */
    void updateMemberStatus(Long memberId, MemberStatusEnum status);
}
