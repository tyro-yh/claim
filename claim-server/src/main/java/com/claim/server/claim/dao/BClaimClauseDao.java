package com.claim.server.claim.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.claim.po.BClaimClause;
import com.claim.server.claim.po.BClaimMain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BClaimClauseDao extends BaseMapper<BClaimClause> {

    List<BClaimClause> selectByReportNoAndFeeType(@Param(value = "reportNo") String reportNo,@Param(value = "feeType") String feeType);

    BClaimClause selectByClauseCodeAndItemCode(@Param(value = "reportNo") String reportNo,@Param(value = "clauseCode") String clauseCode,@Param(value = "itemCode") String itemCode);

    void deleteByClaimNo(@Param(value = "claimNo") String claimNo);
}
