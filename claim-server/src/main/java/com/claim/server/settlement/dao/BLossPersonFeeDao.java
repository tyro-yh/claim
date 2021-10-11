package com.claim.server.settlement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.settlement.po.BLossPersonFee;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BLossPersonFeeDao extends BaseMapper<BLossPersonFee> {

    void deleteBySettlementNo(@Param(value = "reportNo") String reportNo, @Param(value = "settlementNo") String settlementNo);

    BigDecimal getSumHasPaid(@Param(value = "reportNo") String reportNo, @Param(value = "clauseCode") String clauseCode, @Param(value = "itemCode") String itemCode);

    List<BLossPersonFee> selectByLossPersonId(@Param(value = "lossPersonId") Integer lossPersonId);
}
