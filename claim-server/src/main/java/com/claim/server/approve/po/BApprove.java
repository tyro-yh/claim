package com.claim.server.approve.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName(value = "b_approve")
public class BApprove implements Serializable {

    @TableId(value = "ApproveNo",type = IdType.AUTO)
    private Integer approveNo;

    @TableField(value = "PreApproveNo")
    private Integer preApproveNo;

    @TableField(value = "ApproveNode")
    private String approveNode;

    @TableField(value = "ReportNo")
    private String reportNo;

    @TableField(value = "ClaimNo")
    private String claimNo;

    @TableField(value = "SettlementNo")
    private String settlementNo;

    @TableField(value = "EndCaseId")
    private Integer endCaseId;

    @TableField(value = "PersonId")
    private Integer personId;

    @TableField(value = "ApproveFlag")
    private String approveFlag;

    @TableField(value = "ApproveTypeCode")
    private String approveTypeCode;

    @TableField(value = "ApproveTypeName")
    private String approveTypeName;

    @TableField(value = "Content")
    private String content;

    @TableField(value = "ApproverCode")
    private String approverCode;

    @TableField(value = "ApproverName")
    private String approverName;

    @TableField(value = "ApproveDate")
    private Date approveDate;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private List<String> preContent;
}
