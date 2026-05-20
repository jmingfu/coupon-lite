package com.coupon.service;

import com.coupon.dto.MemberStatusDTO;

/**
 * 后台会员管理服务接口
 *
 * @author JMF
 * @date 2026-05-20
 */
public interface AdminMemberService {

    /**
     * 更新会员状态（启用/禁用）
     * @param statusDTO 状态参数（含会员ID和目标状态）
     * @return 是否成功
     */
    Boolean updateMemberStatus(MemberStatusDTO statusDTO);
}
