package com.my.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 认证记录表
 * @TableName verify_record
 */
@TableName(value ="verify_record")
@Data
public class VerifyRecord implements Serializable {
    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 认证类型：1-身份证，2-驾驶证
     */
    private Integer verifyType;

    /**
     * 认证状态：0-未认证，1-认证中，2-已认证，3-认证失败
     */
    private Integer verifyStatus;

    /**
     * 认证结果：1-通过，2-拒绝
     */
    private Integer verifyResult;

    /**
     * 拒绝原因
     */
    private String rejectReason;

    /**
     * 认证时间
     */
    private Date verifyTime;

    /**
     * 审核人ID
     */
    private Long reviewId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}