package com.claim.server.report.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "b_report_main")
public class BReportMain implements Serializable {

    @TableId(value = "ReportNo",type = IdType.INPUT)
    private String reportNo;

    @TableField(value = "ReportTime")
    private Date reportTime;

    @TableField(value = "CaseFlag")
    private String caseFlag;

    @TableField(value = "PolicyNo")
    private String policyNo;

    @TableField(value = "InsuranceName")
    private String insuranceName;

    @TableField(value = "InsuranceCode")
    private String insuranceCode;

    @TableField(value = "InsuredCode")
    private String insuredCode;

    @TableField(value = "InsuredName")
    private String insuredName;

    @TableField(value = "PolicyType")
    private String policyType;

    @TableField(value = "ComCode")
    private String comCode;

    @TableField(value = "ComName")
    private String comName;

    @TableField(value = "reportLoss")
    private BigDecimal ReportLoss;

    @TableField(value = "DamageCode")
    private String damageCode;

    @TableField(value = "DamageTime")
    private Date damageTime;

    @TableField(value = "Province")
    private String province;

    @TableField(value = "City")
    private String city;

    @TableField(value = "CountyCode")
    private String countyCode;

    @TableField(value = "DamageAddress")
    private String damageAddress;

    @TableField(value = "ReportorName")
    private String reportorName;

    @TableField(value = "ReportorPhone")
    private String reportorPhone;

    @TableField(value = "LinkerName")
    private String linkerName;

    @TableField(value = "LinkerPhone")
    private String linkerPhone;

    @TableField(value = "Remark")
    private String remark;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
