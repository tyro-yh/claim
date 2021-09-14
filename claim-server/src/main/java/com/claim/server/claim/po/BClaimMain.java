package com.claim.server.claim.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "b_claim_main")
public class BClaimMain implements Serializable {

    @TableId(value = "ClaimNo",type = IdType.INPUT)
    private String claimNo;

    @TableField(value = "ReportNo")
    private String reportNo;

    @TableField(value = "PolicyNo")
    private String policyNo;

    @TableField(value = "CaseNo")
    private String caseNo;

    @TableField(value = "EndCaseTime")
    private Date endCaseTime;

    @TableField(value = "ClaimStatus")
    private String claimStatus;

    @TableField(value = "claimTime")
    private Date claimTime;

    @TableField(value = "InsuranceName")
    private String insuranceName;

    @TableField(value = "InsuranceCode")
    private String insuranceCode;

    @TableField(value = "InsuredCode")
    private String insuredCode;

    @TableField(value = "InsuredName")
    private String insuredName;

    @TableField(value = "ComCode")
    private String comCode;

    @TableField(value = "ComName")
    private String comName;

    @TableField(value = "reportLoss")
    private BigDecimal reportLoss;

    @TableField(value = "DamageCode")
    private String damageCode;

    @TableField(value = "DamageTime")
    private Date damageTime;

    @TableField(value = "Remark")
    private String remark;

    @TableField(value = "SumDefloss")
    private BigDecimal sumDefloss;

    @TableField(value = "SumClaim")
    private BigDecimal sumClaim;

    @TableField(value = "SumEstiPaid")
    private BigDecimal sumEstiPaid;

    @TableField(value = "SumEstiFee")
    private BigDecimal sumEstiFee;

    @TableField(value = "SumPaid")
    private BigDecimal sumPaid;

    @TableField(value = "SumFee")
    private BigDecimal sumFee;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
