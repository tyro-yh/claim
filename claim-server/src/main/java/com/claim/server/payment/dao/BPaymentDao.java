package com.claim.server.payment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.payment.po.BPayment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BPaymentDao extends BaseMapper<BPayment> {

    void deleteBySettlementNo(@Param(value = "reportNo") String reportNo,@Param(value = "settlementNo") String settlementNo);

    List<BPayment> selectBySettlementNo(@Param(value = "settlementNo") String settlementNo);
}
