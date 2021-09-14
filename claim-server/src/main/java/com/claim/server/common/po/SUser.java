package com.claim.server.common.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "s_user")
public class SUser implements Serializable {

    @TableId(value = "UserCode",type = IdType.INPUT)
    private String userCode;

    @TableField(value = "UserName")
    private String userName;

    @TableField(value = "PassWord")
    private String passWord;

    @TableField(value = "ComCode")
    private String comCode;

    @TableField(value = "MakeCom")
    private String makeCom;

    @TableField(value = "ValidStatus")
    private String validStatus;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
