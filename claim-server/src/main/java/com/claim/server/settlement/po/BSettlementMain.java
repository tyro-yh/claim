package com.claim.server.settlement.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "b_settlement_main")
public class BSettlementMain implements Serializable {

    @TableId(value = "SettlementNo",type = IdType.INPUT)
    private String settlementNo;

    @TableField(value = "ReportNo")
    private String reportNo;

    @TableField(value = "ClaimNo")
    private String claimNo;

    @TableField(value = "PolicyNo")
    private String policyNo;

    @TableField(value = "InsuranceName")
    private String insuranceName;

    @TableField(value = "InsuranceCode")
    private String insuranceCode;

    @TableField(value = "ComCode")
    private String comCode;

    @TableField(value = "ComName")
    private String comName;

    @TableField(value = "SettlementType")
    private String settlementType;

    @TableField(value = "SumAmt")
    private BigDecimal sumAmt;

    @TableField(value = "SumPreAmt")
    private BigDecimal sumPreAmt;

    @TableField(value = "SumPaidAmt")
    private BigDecimal sumPaidAmt;

    @TableField(value = "SumFee")
    private BigDecimal sumFee;

    @TableField(value = "SumRealPay")
    private BigDecimal sumRealPay;

    @TableField(value = "PayStatus")
    private String payStatus;

    @TableField(value = "PayDate")
    private Date payDate;

    @TableField(value = "Content")
    private String content;

    @TableField(value = "HandlerCode")
    private String handlerCode;

    @TableField(value = "HandlerName")
    private String handlerName;

    @TableField(value = "SettlementDate")
    private Date settlementDate;

    @TableField(value = "UnderwriteFlag")
    private String underwriteFlag;

    @TableField(value = "UnderwriteDate")
    private Date underwriteDate;

    @TableField(value = "EndCaseFlag")
    private String endCaseFlag;

    @TableField(value = "LawsuitFlag")
    private String lawsuitFlag;

    @TableField(value = "AllowFlag")
    private String allowFlag;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
