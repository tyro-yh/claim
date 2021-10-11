package com.claim.server.person.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "b_person_main")
public class BPersonMain implements Serializable {

    @TableId(value = "Id",type = IdType.AUTO)
    private Integer id;

    @TableField(value = "ReportNo")
    private String reportNo;

    @TableField(value = "PersonFlag")
    private String personFlag;

    @TableField(value = "LossType")
    private String lossType;

    @TableField(value = "PersonName")
    private String personName;

    @TableField(value = "IdentifyType")
    private String identifyType;

    @TableField(value = "IdentifyNo")
    private String identifyNo;

    @TableField(value = "Phone")
    private String phone;

    @TableField(value = "Sex")
    private String sex;

    @TableField(value = "Age")
    private Integer age;

    @TableField(value = "Birthday")
    private Date birthday;

    @TableField(value = "Content")
    private String content;

    @TableField(value = "SumAmt")
    private BigDecimal sumAmt;

    @TableField(value = "HandlerCode")
    private String handlerCode;

    @TableField(value = "HandlerName")
    private String handlerName;

    @TableField(value = "PersonSubDate")
    private Date personSubDate;

    @TableField(value = "UnderwriteFlag")
    private String underwriteFlag;

    @TableField(value = "UnderwriteDate")
    private Date underwriteDate;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
