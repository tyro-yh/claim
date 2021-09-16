package com.claim.server.datacollection.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName(value = "b_data_collection")
public class BDataCollection implements Serializable {

    @TableId(value = "Id",type = IdType.AUTO)
    private Integer id;

    @TableField(value = "ReportNo")
    private String reportNo;

    @TableField(value = "CertifyCode")
    private String certifyCode;

    @TableField(value = "CertifyName")
    private String certifyName;

    @TableField(value = "CollectTime")
    private Date collectTime;

    @TableField(value = "UploadTime")
    private Date uploadTime;

    @TableField(value = "UploaderCode")
    private String uploaderCode;

    @TableField(value = "UploaderName")
    private String uploaderName;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    List<BFileList> fileList;
}
