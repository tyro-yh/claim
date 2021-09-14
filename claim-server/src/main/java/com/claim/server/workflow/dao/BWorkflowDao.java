package com.claim.server.workflow.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.workflow.po.BWorkflow;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BWorkflowDao extends BaseMapper<BWorkflow> {

    List<Map> initTaskStatus(@Param(value = "userCode") String userCode);

    List<Map> insuranceScale();

    BWorkflow selectCaseMain(@Param(value = "reportNo") String reportNo);

    BWorkflow selectNodeByReportNo(@Param(value = "reportNo") String reportNo,@Param(value = "taskType") String taskType,@Param(value = "businessKey") String businessKey);
}
