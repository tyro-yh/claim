package com.claim.server.workflow.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BWorkflowVo {

    private Integer taskId;

    private Integer parentId;

    private Integer prevTaskId;

    private String taskType;

    private String taskStatus;

    private String handler;

    private String creator;

    private String startTime;

    private String endTime;

    private String comCode;

    private String businessKey;

    private String reportNo;

    private String policyNo;

    private String remark;

    private Date createTime;

    private Date updateTime;

    private List<BWorkflowVo> nextNodes;
}
