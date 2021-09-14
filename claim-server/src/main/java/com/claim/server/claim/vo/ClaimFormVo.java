package com.claim.server.claim.vo;

import com.claim.server.claim.po.BClaimClause;
import com.claim.server.claim.po.BClaimMain;
import lombok.Data;

import java.util.List;

@Data
public class ClaimFormVo {

    private BClaimMain claimMain;

    private List<BClaimClause> claimKindP;

    private List<BClaimClause> claimKindF;
}
