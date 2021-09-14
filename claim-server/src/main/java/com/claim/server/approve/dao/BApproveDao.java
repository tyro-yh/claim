package com.claim.server.approve.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.approve.po.BApprove;
import com.claim.server.workflow.po.BWorkflow;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BApproveDao extends BaseMapper<BApprove> {

    List<BApprove> selectBySettlementNo(@Param(value = "settlementNo") String settlementNo);

    List<BApprove> selectEndCaseDoing(@Param(value = "reportNo") String reportNo);
}
