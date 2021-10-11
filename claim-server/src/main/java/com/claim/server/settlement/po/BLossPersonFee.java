package com.claim.server.settlement.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "b_loss_person_fee")
public class BLossPersonFee implements Serializable {

    @TableId(value = "Id",type = IdType.AUTO)
    private Integer id;

    @TableField(value = "SettlementNo")
    private String settlementNo;

    @TableField(value = "ReportNo")
    private String reportNo;

    @TableField(value = "LossPersonId")
    private Integer lossPersonId;

    @TableField(value = "ClauseName")
    private String clauseName;

    @TableField(value = "ClauseCode")
    private String clauseCode;

    @TableField(value = "ItemName")
    private String itemName;

    @TableField(value = "ItemCode")
    private String itemCode;

    @TableField(value = "FeeTypeCode")
    private String feeTypeCode;

    @TableField(value = "Amount")
    private BigDecimal amount;

    @TableField(value = "SumLoss")
    private BigDecimal sumLoss;

    @TableField(value = "SumLossChecked")
    private BigDecimal sumLossChecked;

    @TableField(value = "ClaimRate")
    private BigDecimal claimRate;

    @TableField(value = "DeductAddRate")
    private BigDecimal deductAddRate;

    @TableField(value = "DeductAddAmt")
    private BigDecimal deductAddAmt;

    @TableField(value = "SumAmt")
    private BigDecimal sumAmt;

    @TableField(value = "SumRealPay")
    private BigDecimal sumRealPay;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
