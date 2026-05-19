package com.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coupon.dto.VerificationRecordAdminDTO;
import com.coupon.dto.VerificationRecordQueryDTO;
import com.coupon.entity.VerificationRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 三表连接查询核销记录（后台管理使用）- 支持MyBatis Plus分页插件
     *
     * @param page  分页对象
     * @param query 查询条件
     * @return 核销记录分页列表
     */
    IPage<VerificationRecordAdminDTO> selectVerificationRecordsWithJoin(
            Page<VerificationRecordAdminDTO> page,
            @Param("query") VerificationRecordQueryDTO query);
}
