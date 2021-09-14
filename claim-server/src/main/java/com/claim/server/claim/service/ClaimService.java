package com.claim.server.claim.service;

import com.claim.server.approve.po.BApprove;
import com.claim.server.claim.po.BClaimMain;
import com.claim.server.claim.vo.ClaimFormVo;

import java.util.Map;

public interface ClaimService {

    Map createClaimInfo(String reportNo);

    Map getClaimInfo(BClaimMain claimMain, Map map);

    void saveClaim(ClaimFormVo vo);

    void submitClaim(ClaimFormVo vo);

    void claimApprove(BApprove approve);

}
