package com.claim.server.menu.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SMenu implements Serializable {

    @TableId(value = "Id",type = IdType.AUTO)
    private Integer id;

    @TableField(value = "Path")
    private String path;

    @TableField(value = "Name")
    private String name;

    @TableField(value = "IconCls")
    private String iconCls;

    @TableField(value = "ParentId")
    private Integer parentId;

    @TableField(value = "BelongGrade")
    private String belongGrade;

    @TableField(value = "Closable")
    private Boolean closable;

    @TableField(value = "Title")
    private String title;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
