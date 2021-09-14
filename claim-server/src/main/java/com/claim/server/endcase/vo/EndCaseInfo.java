package com.claim.server.endcase.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class EndCaseInfo {

    private String reportNo;

    private String claimNo;

    private String caseNo;

    private Date endCaseTime;

    private Date claimTime;

    private String insuranceName;

    private String insuredName;

    private BigDecimal amount;

    private BigDecimal sumClaim;

    private Date damageTime;

    private String damageAddress;

    private String damageCode;

    private String comName;

    private String handler;

    private String endCaser;
}
