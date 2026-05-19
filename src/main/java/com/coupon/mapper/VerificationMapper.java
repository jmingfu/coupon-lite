package com.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coupon.entity.VerificationRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 核销记录Mapper - 使用MyBatis Plus操作
 *
 * @author JMF
 * @date 2026-05-18
 */
@Repository
@Mapper
public interface VerificationMapper extends BaseMapper<VerificationRecord> {
    // 使用MyBatis Plus提供的方法，不再自定义SQL
}