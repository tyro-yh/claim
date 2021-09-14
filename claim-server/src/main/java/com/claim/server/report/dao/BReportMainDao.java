package com.claim.server.report.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.report.po.BReportMain;
import org.apache.ibatis.annotations.Param;

public interface BReportMainDao extends BaseMapper<BReportMain> {

    BReportMain selectByReportNo(@Param(value = "reportNo") String reportNo);

    String selectCaseFlag(@Param(value = "reportNo") String reportNo);
}
