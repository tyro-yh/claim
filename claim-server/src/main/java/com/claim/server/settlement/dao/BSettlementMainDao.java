package com.claim.server.settlement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.settlement.po.BSettlementMain;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BSettlementMainDao extends BaseMapper<BSettlementMain> {

    List<BSettlementMain> selectBySettlementType(@Param(value = "reportNo") String reportNo,@Param(value = "settlementType") String settlementType);

    List<BSettlementMain> selectByReportNo(@Param(value = "reportNo") String reportNo);

    BigDecimal getSumHasPaid(@Param(value = "reportNo") String reportNo);
}
