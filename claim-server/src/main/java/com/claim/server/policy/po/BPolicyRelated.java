package com.claim.server.policy.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "b_policy_related")
public class BPolicyRelated implements Serializable {

    @TableId(value = "Id",type = IdType.UUID)
    private String id;

    @TableField(value = "PolicyNo")
    private String policyNo;

    @TableField(value = "PType")
    private String pType;

    @TableField(value = "RelatedType")
    private String relatedType;

    @TableField(value = "RelatedCode")
    private String relatedCode;

    @TableField(value = "RelatedName")
    private String relatedName;

    @TableField(value = "Sex")
    private String sex;

    @TableField(value = "ValidStatus")
    private String validStatus;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
