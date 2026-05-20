package com.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 后台管理员实体
 *
 * @author JMF
 * @date 2026-05-19
 */
@Data
@TableName("counpon_admin")
public class CouponAdmin {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名（必填，只能包含字母数字下划线4-20位）
     */
    private String username;

    /**
     * 手机号（可选）
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 注册时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除 0否1是
     */
    @TableLogic
    private Integer isDeleted;
}
