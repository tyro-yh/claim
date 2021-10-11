package com.claim.server.settlement.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@TableName(value = "b_loss_person")
public class BLossPerson implements Serializable {

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

    @TableField(value = "PersonId")
    private Integer personId;

    @TableField(value = "PersonName")
    private String personName;

    @TableField(value = "IdentifyType")
    private String identifyType;

    @TableField(value = "IdentifyNo")
    private String identifyNo;

    @TableField(value = "Sex")
    private String sex;

    @TableField(value = "Age")
    private Integer age;

    @TableField(value = "SumAmt")
    private BigDecimal sumAmt;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private List<BLossPersonFee> lossPersonFee;
}
