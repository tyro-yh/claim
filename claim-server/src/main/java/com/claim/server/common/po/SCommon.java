package com.claim.server.common.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "s_common")
public class SCommon implements Serializable {

    @TableId(value = "Id",type = IdType.UUID)
    private String id;

    @TableField(value = "DataTypeCode")
    private String dataTypeCode;

    @TableField(value = "DataTypeName")
    private String dataTypeName;

    @TableField(value = "DataCode")
    private String dataCode;

    @TableField(value = "DataName")
    private String dataName;

    @TableField(value = "PreType")
    private String preType;

    @TableField(value = "PreCode")
    private String preCode;

    @TableField(value = "ValidStatus")
    private String validStatus;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
