package com.claim.server.workflow.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName(value = "b_workflow")
public class BWorkflow implements Serializable {

    @TableId(value = "TaskId",type = IdType.AUTO)
    private Integer taskId;

    @TableField(value = "ParentId")
    private Integer parentId;

    @TableField(value = "PrevTaskId")
    private Integer prevTaskId;

    @TableField(value = "TaskType")
    private String taskType;

    @TableField(value = "TaskStatus")
    private String taskStatus;

    @TableField(value = "Handler")
    private String handler;

    @TableField(value = "Creator")
    private String creator;

    @TableField(value = "StartTime")
    private Date startTime;

    @TableField(value = "EndTime")
    private Date endTime;

    @TableField(value = "ComCode")
    private String comCode;

    @TableField(value = "BusinessKey")
    private String businessKey;

    @TableField(value = "ReportNo")
    private String reportNo;

    @TableField(value = "PolicyNo")
    private String policyNo;

    @TableField(value = "Insurance")
    private String insurance;

    @TableField(value = "Remark")
    private String remark;

    // 第一次添加填充
    @TableField(value = "CreateTime",fill = FieldFill.INSERT)
    private Date createTime;

    // 第一次添加的时候填充，但之后每次更新也会进行填充
    @TableField(value = "UpdateTime",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private List<BWorkflow> nextNodes;
}
