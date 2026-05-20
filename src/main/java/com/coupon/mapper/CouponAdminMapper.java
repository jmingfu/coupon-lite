package com.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coupon.entity.CouponAdmin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 后台管理员Mapper
 *
 * @author JMF
 * @date 2026-05-19
 */
@Repository
@Mapper
public interface CouponAdminMapper extends BaseMapper<CouponAdmin> {
}
