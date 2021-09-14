package com.claim.server.common.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "s_todo")
public class STodo {

    @TableId(value = "Id",type = IdType.UUID)
    private String id;

    @TableField(value = "Title")
    private String title;

    @TableField(value = "Status")
    private Boolean status;

    @TableField(value = "UserCode")
    private String userCode;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
