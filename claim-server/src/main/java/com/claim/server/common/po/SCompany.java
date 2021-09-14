package com.claim.server.common.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "s_company")
public class SCompany implements Serializable {

    @TableId(value = "ComCode",type = IdType.INPUT)
    private String comCode;

    @TableField(value = "ComName")
    private String comName;

    @TableField(value = "UpperComCode")
    private String upperComCode;

    @TableField(value = "ClaimComCode")
    private String claimComCode;

    @TableField(value = "ClaimComName")
    private String claimComName;

    @TableField(value = "ComFullName")
    private String comFullName;

    @TableField(value = "ComLevel")
    private String comLevel;

    @TableField(value = "ValidStatus")
    private String validStatus;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
