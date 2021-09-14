package com.claim.server.process.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "s_process_node")
public class SProcessNode implements Serializable {

    @TableId(value = "Id",type = IdType.AUTO)
    private Integer id;

    @TableField(value = "ProcessId")
    private Integer processId;

    @TableField(value = "NodeId")
    private String nodeId;

    @TableField(value = "Text")
    private String text;

    @TableField(value = "Handler")
    private String handler;

    @TableField(value = "Amount")
    private BigDecimal amount;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
