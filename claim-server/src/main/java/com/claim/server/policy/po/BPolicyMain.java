package com.claim.server.policy.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "b_policy_main")
public class BPolicyMain implements Serializable {

    @TableId(value = "PolicyNo",type = IdType.INPUT)
    private String policyNo;

    @TableField(value = "ProposalNo")
    private String proposalNo;

    @TableField(value = "StartTime")
    private Date startTime;

    @TableField(value = "EndTime")
    private Date endTime;

    @TableField(value = "InsuranceName")
    private String insuranceName;

    @TableField(value = "InsuranceCode")
    private String insuranceCode;

    @TableField(value = "PolicyType")
    private String policyType;

    @TableField(value = "ComCode")
    private String comCode;

    @TableField(value = "ComName")
    private String comName;

    @TableField(value = "AppliCode")
    private String appliCode;

    @TableField(value = "AppliName")
    private String appliName;

    @TableField(value = "InsuredCode")
    private String insuredCode;

    @TableField(value = "InsuredName")
    private String insuredName;

    @TableField(value = "SumAmount")
    private BigDecimal sumAmount;

    @TableField(value = "SumPremium")
    private BigDecimal sumPremium;

    @TableField(value = "PolicyStatus")
    private String policyStatus;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
