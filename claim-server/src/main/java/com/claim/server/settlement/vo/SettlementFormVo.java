package com.claim.server.settlement.vo;

import com.claim.server.payment.po.BPayment;
import com.claim.server.settlement.po.BLossCharge;
import com.claim.server.settlement.po.BLossPerson;
import com.claim.server.settlement.po.BLossProp;
import com.claim.server.settlement.po.BSettlementMain;
import lombok.Data;

import java.util.List;

@Data
public class SettlementFormVo {

    private BSettlementMain settlementMain;

    private List<BPayment> paymentList;

    private List<BLossProp> lossProps;

    private List<BLossCharge> lossCharges;

    private List<BLossPerson> lossPersonList;
}
