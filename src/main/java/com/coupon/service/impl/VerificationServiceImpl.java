package com.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coupon.common.enums.CouponStatusEnum;
import com.coupon.common.exception.ReturnException;
import com.coupon.common.util.CouponValidUtil;
import com.coupon.common.util.MemberUtil;
import com.coupon.dto.MemberDTO;
import com.coupon.dto.VerificationDTO;
import com.coupon.dto.VerificationRecordAdminDTO;
import com.coupon.dto.VerificationRecordQueryDTO;
import com.coupon.entity.CouponTemplate;
import com.coupon.entity.MemberCoupon;
import com.coupon.entity.VerificationRecord;
import com.coupon.mapper.CouponMapper;
import com.coupon.mapper.MemberCouponMapper;
import com.coupon.mapper.VerificationMapper;
import com.coupon.service.VerificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 核销服务实现类 - 使用MyBatis Plus操作数据库
 *
 * @author JMF
 * @date 2026-05-18
 */
@Service
public class VerificationServiceImpl implements VerificationService {

    @Autowired
    private VerificationMapper verificationMapper;

    @Autowired
    private MemberCouponMapper memberCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    /**
     * 生成唯一的核销码（使用UUID去掉横线）
     */
    private String generateUniqueCode() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VerificationDTO generateVerification(Long templateId) {
        MemberDTO memberInfo = MemberUtil.getMemberInfo();

        // 查询优惠券模板信息
        CouponTemplate couponTemplate = couponMapper.selectById(templateId);
        if (couponTemplate == null) {
            throw new ReturnException("优惠券模板不存在");
        }

        // 检查优惠券是否过期
        CouponValidUtil.checkValid(couponTemplate);

        // 检查用户是否领取过该优惠券
        LambdaQueryWrapper<MemberCoupon> couponWrapper = new LambdaQueryWrapper<>();
        couponWrapper.eq(MemberCoupon::getMemberId, memberInfo.getId())
                .eq(MemberCoupon::getTemplateId, templateId);
        MemberCoupon memberCoupon = memberCouponMapper.selectOne(couponWrapper);

        if (memberCoupon == null) {
            throw new ReturnException("您未领取该优惠券");
        }

        // 检查优惠券状态（使用枚举）
        CouponStatusEnum status = memberCoupon.getStatus();
        if (status != CouponStatusEnum.UNUSED) {
            if (status == CouponStatusEnum.PENDING_VERIFICATION) {
                throw new ReturnException("优惠券已处于待核销状态");
            } else if (status == CouponStatusEnum.VERIFIED) {
                throw new ReturnException("优惠券已核销");
            } else if (status == CouponStatusEnum.EXPIRED) {
                throw new ReturnException("优惠券已过期");
            } else {
                throw new ReturnException("优惠券状态异常");
            }
        }

        // 检查是否已经有核销记录（使用memberId和templateId联合查询）
        LambdaQueryWrapper<VerificationRecord> recordWrapper = new LambdaQueryWrapper<>();
        recordWrapper.eq(VerificationRecord::getMemberId, memberInfo.getId())
                .eq(VerificationRecord::getTemplateId, templateId);
        VerificationRecord existingRecord = verificationMapper.selectOne(recordWrapper);
        if (existingRecord != null) {
            // 返回已有的核销记录
            VerificationDTO dto = new VerificationDTO();
            BeanUtils.copyProperties(existingRecord, dto);
            dto.setCouponName(couponTemplate.getName());
            dto.setCouponType(couponTemplate.getType().ordinal() + 1);
            return dto;
        }

        // 生成唯一核销码
        String verificationCode = generateUniqueCode();

        // 创建核销记录（仅生成核销码，不设置核销人和核销时间）
        VerificationRecord record = new VerificationRecord();
        record.setMemberId(memberInfo.getId());
        record.setTemplateId(templateId);
        record.setVerificationCode(verificationCode);

        // 保存核销记录（使用MyBatis Plus）
        verificationMapper.insert(record);

        // 更新用户优惠券状态为待核销（状态流转：已领取->待核销）
        memberCoupon.setStatus(CouponStatusEnum.PENDING_VERIFICATION);
        memberCoupon.setUseTime(LocalDateTime.now());
        memberCouponMapper.updateById(memberCoupon);

        // 返回核销信息
        VerificationDTO dto = new VerificationDTO();
        BeanUtils.copyProperties(record, dto);
        dto.setCouponName(couponTemplate.getName());
        dto.setCouponType(couponTemplate.getType().ordinal() + 1);

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VerificationDTO confirmVerification(String verificationCode) {
        // 根据核销码查询核销记录
        LambdaQueryWrapper<VerificationRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VerificationRecord::getVerificationCode, verificationCode);
        VerificationRecord record = verificationMapper.selectOne(wrapper);

        if (record == null) {
            throw new ReturnException("核销码不存在");
        }

        // 查询用户优惠券
        LambdaQueryWrapper<MemberCoupon> couponWrapper = new LambdaQueryWrapper<>();
        couponWrapper.eq(MemberCoupon::getMemberId, record.getMemberId())
                .eq(MemberCoupon::getTemplateId, record.getTemplateId());
        MemberCoupon memberCoupon = memberCouponMapper.selectOne(couponWrapper);

        if (memberCoupon == null) {
            throw new ReturnException("用户未领取该优惠券");
        }

        // 检查优惠券状态，必须是待核销状态才能确认核销
        CouponStatusEnum status = memberCoupon.getStatus();
        if (status != CouponStatusEnum.PENDING_VERIFICATION) {
            if (status == CouponStatusEnum.UNUSED) {
                throw new ReturnException("优惠券尚未进入待核销状态");
            } else if (status == CouponStatusEnum.VERIFIED) {
                throw new ReturnException("优惠券已核销");
            } else if (status == CouponStatusEnum.EXPIRED) {
                throw new ReturnException("优惠券已过期");
            } else {
                throw new ReturnException("优惠券状态异常");
            }
        }

        // 检查优惠券是否在有效期内
        CouponTemplate couponTemplate = couponMapper.selectById(record.getTemplateId());
        if (couponTemplate != null) {
            CouponValidUtil.checkValid(couponTemplate);
        }

        // 更新优惠券状态为已核销（状态流转：待核销->已核销）
        memberCoupon.setStatus(CouponStatusEnum.VERIFIED);
        memberCouponMapper.updateById(memberCoupon);

        // 更新核销记录的核销时间和核销人（模拟店员）
        record.setVerifier("STAFF"); // 店员
        record.setVerificationTime(LocalDateTime.now());
        verificationMapper.updateById(record);

        // 返回核销信息
        VerificationDTO dto = new VerificationDTO();
        BeanUtils.copyProperties(record, dto);
        if (couponTemplate != null) {
            dto.setCouponName(couponTemplate.getName());
            dto.setCouponType(couponTemplate.getType().ordinal() + 1);
        }

        return dto;
    }

    @Override
    public List<VerificationDTO> listMyVerifications() {
        MemberDTO memberInfo = MemberUtil.getMemberInfo();

        // 使用MyBatis Plus查询核销记录列表
        LambdaQueryWrapper<VerificationRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VerificationRecord::getMemberId, memberInfo.getId())
                .orderByDesc(VerificationRecord::getCreateTime);
        List<VerificationRecord> records = verificationMapper.selectList(wrapper);

        // 转换为DTO列表
        return records.stream().map(record -> {
            VerificationDTO dto = new VerificationDTO();
            BeanUtils.copyProperties(record, dto);

            // 查询优惠券模板信息
            CouponTemplate couponTemplate = couponMapper.selectById(record.getTemplateId());
            if (couponTemplate != null) {
                dto.setCouponName(couponTemplate.getName());
                dto.setCouponType(couponTemplate.getType().ordinal() + 1);
            }

            return dto;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public IPage<VerificationRecordAdminDTO> pageVerificationRecords(VerificationRecordQueryDTO query) {
        // 设置默认分页参数
        if (query.getPageNum() == null || query.getPageNum() <= 0) {
            query.setPageNum(1);
        }
        if (query.getPageSize() == null || query.getPageSize() <= 0) {
            query.setPageSize(10);
        }

        // 创建分页对象
        Page<VerificationRecordAdminDTO> page = new Page<>(query.getPageNum(), query.getPageSize());

        // 使用MyBatis Plus分页插件，自动处理分页
        return verificationMapper.selectVerificationRecordsWithJoin(page, query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VerificationDTO refreshCode(Long recordId) {
        MemberDTO memberInfo = MemberUtil.getMemberInfo();

        VerificationRecord record = verificationMapper.selectById(recordId);
        if (record == null) {
            throw new ReturnException("核销记录不存在");
        }
        if (!record.getMemberId().equals(memberInfo.getId())) {
            throw new ReturnException("无权操作该核销记录");
        }

        LambdaQueryWrapper<MemberCoupon> couponWrapper = new LambdaQueryWrapper<>();
        couponWrapper.eq(MemberCoupon::getMemberId, record.getMemberId())
                .eq(MemberCoupon::getTemplateId, record.getTemplateId());
        MemberCoupon memberCoupon = memberCouponMapper.selectOne(couponWrapper);

        if (memberCoupon == null || memberCoupon.getStatus() != CouponStatusEnum.PENDING_VERIFICATION) {
            throw new ReturnException("仅待核销状态可刷新核销码");
        }

        String newCode = generateUniqueCode();
        record.setVerificationCode(newCode);
        verificationMapper.updateById(record);

        CouponTemplate couponTemplate = couponMapper.selectById(record.getTemplateId());
        VerificationDTO dto = new VerificationDTO();
        BeanUtils.copyProperties(record, dto);
        if (couponTemplate != null) {
            dto.setCouponName(couponTemplate.getName());
            dto.setCouponType(couponTemplate.getType().ordinal() + 1);
        }
        return dto;
    }
}
