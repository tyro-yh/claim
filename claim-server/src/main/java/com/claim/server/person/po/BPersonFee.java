package com.claim.server.person.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "b_person_fee")
public class BPersonFee implements Serializable {

    @TableId(value = "Id",type = IdType.AUTO)
    private Integer id;

    @TableField(value = "ReportNo")
    private String reportNo;

    @TableField(value = "PersonId")
    private Integer personId;

    @TableField(value = "FeeTypeCode")
    private String feeTypeCode;

    @TableField(value = "ClaimLoss")
    private BigDecimal claimLoss;

    @TableField(value = "DeductionFee")
    private BigDecimal deductionFee;

    @TableField(value = "EstimateLoss")
    private BigDecimal estimateLoss;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
