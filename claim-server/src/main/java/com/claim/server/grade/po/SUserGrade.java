package com.claim.server.grade.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "s_user_grade")
public class SUserGrade implements Serializable {

    @TableId(value = "Id",type = IdType.AUTO)
    private String id;

    @TableField(value = "UserName")
    private String userName;

    @TableField(value = "UserCode")
    private String userCode;

    @TableField(value = "GradeCName")
    private String gradeCName;

    @TableField(value = "GradeEName")
    private String gradeEName;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
