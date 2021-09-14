package com.claim.server.policy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.policy.po.BPolicyMain;
import org.apache.ibatis.annotations.Param;

public interface BPolicyMainDao extends BaseMapper<BPolicyMain> {

    BPolicyMain selectByPolicyNo(@Param(value = "policyNo") String policyNo);

    BPolicyMain selectByReportNo(@Param(value = "reportNo") String reportNo);


}
