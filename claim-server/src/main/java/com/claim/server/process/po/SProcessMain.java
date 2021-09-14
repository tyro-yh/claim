package com.claim.server.process.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "s_process_main")
public class SProcessMain implements Serializable {

    @TableId(value = "Id",type = IdType.AUTO)
    private Integer id;

    @TableField(value = "ProcessType")
    private String processType;

    @TableField(value = "Title")
    private String title;

    @TableField(value = "Insurance")
    private String insurance;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
