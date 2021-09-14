package com.claim.server.settlement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.settlement.po.BLossCharge;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BLossChargeDao extends BaseMapper<BLossCharge> {

    void deleteBySettlementNo(@Param(value = "reportNo") String reportNo, @Param(value = "settlementNo") String settlementNo);

    List<BLossCharge> selectBySettlementNo(@Param(value = "settlementNo") String settlementNo);
}
