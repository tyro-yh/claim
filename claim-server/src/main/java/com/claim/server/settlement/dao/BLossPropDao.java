package com.claim.server.settlement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.settlement.po.BLossProp;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BLossPropDao extends BaseMapper<BLossProp> {

    void deleteBySettlementNo(@Param(value = "reportNo") String reportNo, @Param(value = "settlementNo") String settlementNo);

    BigDecimal getSumHasPaid(@Param(value = "reportNo") String reportNo,@Param(value = "clauseCode") String clauseCode,@Param(value = "itemCode") String itemCode);

    List<BLossProp> selectBySettlementNo(@Param(value = "settlementNo") String settlementNo);
}
