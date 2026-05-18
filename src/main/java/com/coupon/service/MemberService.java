package com.coupon.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coupon.dto.MemberDTO;

/**
 * 基于SpringBoot框架的个人练手项目-
 *
 * @author JMF
 * @date 2026-04-05 16:22
 * @date 2026-04-05
 */

public interface MemberService {
    MemberDTO   wxLogin(MemberDTO memberDTO)throws Exception;
    MemberDTO getById(Long id);
    IPage<MemberDTO> selectPage(MemberDTO memberDTO);
}
