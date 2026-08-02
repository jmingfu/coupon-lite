package com.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coupon.entity.Member;
import com.coupon.entity.MemberLoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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
public interface MemberLoginLogMapper extends BaseMapper<MemberLoginLog> {

}
