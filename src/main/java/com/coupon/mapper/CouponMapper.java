package com.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coupon.dto.CouponDTO;
import com.coupon.entity.CouponTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 基于SpringBoot框架的个人练手项目-
 *
 * @author JMF
 * @date 2026-04-05 17:08
 * @date 2026-04-05
 */
//@Api(tags = "会员mapper类")
@Repository
@Mapper
public interface CouponMapper extends BaseMapper<CouponTemplate> {
    int decreaseAmount(@Param("id") Long id);

    List<CouponDTO> getMyCoupons(@Param("memberId") Long memberId);
}
