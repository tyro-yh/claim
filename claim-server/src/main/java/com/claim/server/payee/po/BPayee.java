package com.claim.server.payee.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

@Data
@TableName(value = "b_payee")
public class BPayee implements Serializable {

    @TableId(value = "Id",type = IdType.AUTO)
    private Integer id;

    @TableField(value = "ReportNo")
    private String reportNo;

    @TableField(value = "PayeeName")
    private String payeeName;

    @TableField(value = "PayeeType")
    private String payeeType;

    @TableField(value = "PayeeKind")
    private String payeeKind;

    @TableField(value = "PayeePhone")
    private String payeePhone;

    @TableField(value = "CertifyType")
    private String certifyType;

    @TableField(value = "CertifyNo")
    private String certifyNo;

    @TableField(value = "AccountType")
    private String accountType;

    @TableField(value = "AccountNo")
    private String accountNo;

    @TableField(value = "BankName")
    private String bankName;

    @TableField(value = "BankCode")
    private String bankCode;

    @TableField(value = "BankOutlets")
    private String bankOutlets;

    @TableField(value = "BankNo")
    private String bankNo;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}