package com.claim.server.endcase.service;

import com.claim.server.approve.po.BApprove;
import com.claim.server.endcase.vo.EndCaseInfo;

import java.util.Map;

public interface EndCaseService {

    Map applyEndCase(String reportNo);

    EndCaseInfo initEndCaseInfo(String reportNo,String businessKey);

    void endCase(BApprove approve);
}
