package com.claim.server.policy.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@TableName(value = "b_policy_clause")
public class BPolicyClause implements Serializable {

    @TableId(value = "Id",type = IdType.AUTO)
    private Integer id;

    @TableField(value = "PolicyNo")
    private String policyNo;

    @TableField(value = "InsuranceName")
    private String insuranceName;

    @TableField(value = "InsuranceCode")
    private String insuranceCode;

    @TableField(value = "FeeType")
    private String feeType;

    @TableField(value = "KindFlag")
    private String kindFlag;

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

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private List<BPolicyClause> itemList;

}
