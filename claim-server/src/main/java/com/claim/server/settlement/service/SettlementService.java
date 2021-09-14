package com.claim.server.settlement.service;

import com.claim.server.approve.po.BApprove;
import com.claim.server.settlement.vo.SettlementFormVo;

import java.util.Map;

public interface SettlementService {

    Map createSettlementInfo(String reportNo,String settlementType);

    Map initSettlementInfo(String reportNo,String settlementNo);

    void buildContent(SettlementFormVo params);

    void saveSettlement(SettlementFormVo params);

    void submitSettlement(SettlementFormVo params);

    void cancelSettlement(String settlementNo);

    void undwrt(BApprove approve);

    Map initSettlementForUndwrt(String reportNo,String businessKey);

    Map checkSettlementForAdd(String reportNo,String settlementType);

    Map checkSettlementForSubmit(SettlementFormVo params);
}
