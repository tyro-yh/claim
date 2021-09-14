package com.claim.server.endcase.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "b_endcase")
public class BEndCase implements Serializable {

    @TableId(value = "Id",type = IdType.AUTO)
    private Integer id;

    @TableField(value = "ReportNo")
    private String reportNo;

    @TableField(value = "ClaimNo")
    private String claimNo;

    @TableField(value = "CaseNo")
    private String caseNo;

    @TableField(value = "InsuranceName")
    private String insuranceName;

    @TableField(value = "InsuranceCode")
    private String insuranceCode;

    @TableField(value = "EndCaseTime")
    private Date endCaseTime;

    @TableField(value = "HandlerCode")
    private String handlerCode;

    @TableField(value = "HandlerName")
    private String handlerName;

    @TableField(value = "EndCaserCode")
    private String endCaserCode;

    @TableField(value = "EndCaserName")
    private String endCaserName;

    @TableField(value = "SumClaim")
    private BigDecimal sumClaim;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
