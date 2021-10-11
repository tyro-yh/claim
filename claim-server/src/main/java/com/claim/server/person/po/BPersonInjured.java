package com.claim.server.person.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "b_person_injured")
public class BPersonInjured implements Serializable {

    @TableId(value = "Id",type = IdType.AUTO)
    private Integer id;

    @TableField(value = "ReportNo")
    private String reportNo;

    @TableField(value = "PersonId")
    private Integer personId;

    @TableField(value = "InjuredPart")
    private String injuredPart;

    @TableField(value = "DiagnosisName")
    private String diagnosisName;

    @TableField(value = "DiagnosisCode")
    private String diagnosisCode;

    @TableField(value = "Treatment")
    private String treatment;

    @TableField(value = "TreatWay")
    private String treatWay;

    @TableField(value = "SpecificDiagnosis")
    private String specificDiagnosis;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
