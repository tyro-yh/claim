package com.claim.server.payment.po;

import com.baomidou.mybatisplus.annotation.*;
import com.claim.server.payee.po.BPayee;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "b_payment")
public class BPayment implements Serializable {

    @TableId(value = "Id",type = IdType.AUTO)
    private Integer id;

    @TableField(value = "ReportNo")
    private String reportNo;

    @TableField(value = "SettlementNo")
    private String settlementNo;

    @TableField(value = "PayeeId")
    private Integer payeeId;

    @TableField(value = "Amount")
    private BigDecimal amount;

    @TableField(value = "Remark")
    private String remark;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private BPayee payeeInfo;
}
