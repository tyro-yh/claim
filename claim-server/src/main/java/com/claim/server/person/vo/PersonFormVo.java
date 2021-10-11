package com.claim.server.person.vo;

import com.claim.server.person.po.BPersonFee;
import com.claim.server.person.po.BPersonInjured;
import com.claim.server.person.po.BPersonMain;
import com.claim.server.person.po.BPersonTrace;
import lombok.Data;

import java.util.List;

@Data
public class PersonFormVo {

    private BPersonMain personMain;

    private List<BPersonInjured> injuredList;

    private List<BPersonTrace> traceList;

    private List<BPersonFee> feeList;
}
