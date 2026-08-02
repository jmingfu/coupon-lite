package com.coupon.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 会员登录记录表
 *
 * @TableName member_login_log
 */
@TableName("member_login_log")
@Data
public class MemberLoginLog implements Serializable {

    /**
     * 主键ID
     */
    @NotNull(message = "[主键ID]不能为空")
    @ApiModelProperty("主键ID")
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 会员ID（关联member表）
     */
    @NotNull(message = "[会员ID（关联member表）]不能为空")
    @ApiModelProperty("会员ID（关联member表）")
    private Long memberId;
    /**
     * 登录时间
     */
    @ApiModelProperty("登录时间")
    private LocalDateTime createTime;
    /**
     * 登录欢迎语
     */
    @Size(max = 128, message = "编码长度不能超过128")
    @ApiModelProperty("登录欢迎语")
    @Length(max = 128, message = "编码长度不能超过128")
    private String welcomeMsg;


}
