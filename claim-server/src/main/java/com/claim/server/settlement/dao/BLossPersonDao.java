package com.claim.server.settlement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.settlement.po.BLossPerson;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BLossPersonDao extends BaseMapper<BLossPerson> {

    void deleteBySettlementNo(@Param(value = "reportNo") String reportNo, @Param(value = "settlementNo") String settlementNo);

    List<BLossPerson> selectBySettlementNo(@Param(value = "settlementNo") String settlementNo);
}
