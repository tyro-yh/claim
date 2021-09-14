package com.claim.server.claim.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "b_claim_clause")
public class BClaimClause implements Serializable {

    @TableId(value = "Id",type = IdType.AUTO)
    private Integer id;

    @TableField(value = "ClaimNo")
    private String claimNo;

    @TableField(value = "ReportNo")
    private String reportNo;

    @TableField(value = "InsuredCode")
    private String insuredCode;

    @TableField(value = "InsuredName")
    private String insuredName;

    @TableField(value = "FeeType")
    private String feeType;

    @TableField(value = "ClauseName")
    private String clauseName;

    @TableField(value = "ClauseCode")
    private String clauseCode;

    @TableField(value = "ItemName")
    private String itemName;

    @TableField(value = "ItemCode")
    private String itemCode;

    @TableField(value = "DeductAddRate")
    private BigDecimal deductAddRate;

    @TableField(value = "DeductAddAmt")
    private BigDecimal deductAddAmt;

    @TableField(value = "Amount")
    private BigDecimal amount;

    @TableField(value = "SumEstiPaid")
    private BigDecimal sumEstiPaid;

    @TableField(value = "CostType")
    private String costType;

    @TableField(value = "SumEstiFee")
    private BigDecimal sumEstiFee;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
