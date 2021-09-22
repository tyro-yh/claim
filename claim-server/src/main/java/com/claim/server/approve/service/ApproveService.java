package com.claim.server.approve.service;

import com.claim.server.approve.po.BApprove;
import com.claim.server.utils.TaskTypeEnum;

import java.util.List;
import java.util.Map;

public interface ApproveService {

    Integer createApprove(BApprove bApprove);

    BApprove initApproveInfo(Integer approveNo, List<String> preContent);

    Map saveApprove(BApprove bApprove);

    Map submitApprove(BApprove bApprove);

    List<BApprove> getApproveForSettlement(String settlementNo);

    List<BApprove> selectEndCaseDoing(String reportNo);
}
