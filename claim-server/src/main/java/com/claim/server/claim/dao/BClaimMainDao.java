package com.claim.server.claim.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.claim.po.BClaimMain;
import org.apache.ibatis.annotations.Param;

public interface BClaimMainDao extends BaseMapper<BClaimMain> {

    BClaimMain selectByReportNo(@Param(value = "reportNo") String reportNo);
}
