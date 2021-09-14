package com.claim.server.settlement.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "b_loss_charge")
public class BLossCharge implements Serializable {

    @TableId(value = "Id",type = IdType.AUTO)
    private Integer id;

    @TableField(value = "SettlementNo")
    private String settlementNo;

    @TableField(value = "ReportNo")
    private String reportNo;

    @TableField(value = "ClaimNo")
    private String claimNo;

    @TableField(value = "SettlementType")
    private String settlementType;

    @TableField(value = "ClauseName")
    private String clauseName;

    @TableField(value = "ClauseCode")
    private String clauseCode;

    @TableField(value = "CostType")
    private String costType;

    @TableField(value = "PayObject")
    private String payObject;

    @TableField(value = "SumLoss")
    private BigDecimal sumLoss;

    @TableField(value = "SumRealPay")
    private BigDecimal sumRealPay;

    @TableField(value = "Remark")
    private String remark;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
