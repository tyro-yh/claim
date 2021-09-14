package com.claim.server.policy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.policy.po.BPolicyClause;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BPolicyClauseDao extends BaseMapper<BPolicyClause> {

    List<BPolicyClause> selectClauseByPolicyNo(@Param(value = "policyNo") String policyNo, @Param(value = "feeType") String feeType);

    List<BPolicyClause> selectItems(@Param(value = "policyNo") String policyNo, @Param(value = "clauseCode") String clauseCode);
}
