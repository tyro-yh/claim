package com.claim.server.settlement.service.impl;

import com.claim.server.approve.dao.BApproveDao;
import com.claim.server.approve.po.BApprove;
import com.claim.server.approve.service.ApproveService;
import com.claim.server.claim.dao.BClaimClauseDao;
import com.claim.server.claim.dao.BClaimMainDao;
import com.claim.server.claim.po.BClaimClause;
import com.claim.server.claim.po.BClaimMain;
import com.claim.server.common.po.SUser;
import com.claim.server.endcase.service.EndCaseService;
import com.claim.server.payee.dao.BPayeeDao;
import com.claim.server.payee.po.BPayee;
import com.claim.server.payment.dao.BPaymentDao;
import com.claim.server.payment.po.BPayment;
import com.claim.server.policy.dao.BPolicyMainDao;
import com.claim.server.policy.po.BPolicyMain;
import com.claim.server.process.dao.SProcessMainDao;
import com.claim.server.process.dao.SProcessNodeDao;
import com.claim.server.process.po.SProcessMain;
import com.claim.server.process.po.SProcessNode;
import com.claim.server.process.service.ProcessService;
import com.claim.server.report.dao.BReportMainDao;
import com.claim.server.report.po.BReportMain;
import com.claim.server.settlement.dao.*;
import com.claim.server.settlement.po.*;
import com.claim.server.settlement.service.SettlementService;
import com.claim.server.settlement.vo.SettlementFormVo;
import com.claim.server.utils.*;
import com.claim.server.workflow.dao.BWorkflowDao;
import com.claim.server.workflow.po.BWorkflow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SettlementServiceImpl implements SettlementService {

    @Autowired
    private BSettlementMainDao bSettlementMainDao;

    @Autowired
    private BClaimMainDao bClaimMainDao;

    @Autowired
    private BClaimClauseDao bClaimClauseDao;

    @Autowired
    private BPaymentDao bPaymentDao;

    @Autowired
    private BWorkflowDao workflowDao;

    @Autowired
    private SProcessMainDao sProcessMainDao;

    @Autowired
    private SProcessNodeDao sProcessNodeDao;

    @Autowired
    private ApproveService approveService;

    @Autowired
    private BApproveDao bApproveDao;

    @Autowired
    private BLossPropDao bLossPropDao;

    @Autowired
    private BLossChargeDao bLossChargeDao;

    @Autowired
    private BPayeeDao bPayeeDao;

    @Autowired
    private BPolicyMainDao bPolicyMainDao;

    @Autowired
    private BReportMainDao bReportMainDao;

    @Autowired
    private ProcessService processService;

    @Autowired
    private EndCaseService endCaseService;

    @Autowired
    private BLossPersonDao bLossPersonDao;

    @Autowired
    private BLossPersonFeeDao bLossPersonFeeDao;

    @Override
    public Map createSettlementInfo(String reportNo,String settlementType) {
        Map map = new HashMap();

        SUser user = CommonUtil.getLoginUser();
        BClaimMain claimMain = bClaimMainDao.selectByReportNo(reportNo);

        BSettlementMain settlementMain = new BSettlementMain();
        //todo ????????????????????????????????????????????????
        String settlementNo = "S"+settlementType+CommonUtil.formatDateTime(new Date(),"yyyyMMddHHmmss")+"01";
        settlementMain.setSettlementNo(settlementNo);
        settlementMain.setReportNo(claimMain.getReportNo());
        settlementMain.setClaimNo(claimMain.getClaimNo());
        settlementMain.setPolicyNo(claimMain.getPolicyNo());
        settlementMain.setComCode(claimMain.getComCode());
        settlementMain.setComName(claimMain.getComName());
        settlementMain.setInsuranceCode(claimMain.getInsuranceCode());
        settlementMain.setInsuranceName(claimMain.getInsuranceName());
        settlementMain.setSettlementType(settlementType);
        settlementMain.setHandlerCode(user.getUserCode());
        settlementMain.setHandlerName(user.getUserName());
        settlementMain.setUnderwriteFlag(UnderwriteFlagEnum.INITORBACK.getCode());
        settlementMain.setEndCaseFlag("0");
        settlementMain.setAllowFlag("0");
        settlementMain.setLawsuitFlag("0");
        map.put("settlementMain",settlementMain);

        getClaimKindP(reportNo,map);
        getClaimKindF(reportNo,map);

        return map;
    }

    @Override
    public Map initSettlementInfo(String reportNo,String settlementNo) {
        Map map = new HashMap();
        BSettlementMain settlementMain = bSettlementMainDao.selectById(settlementNo);
        map.put("settlementMain",settlementMain);

        getClaimKindP(reportNo,map);
        getClaimKindF(reportNo,map);

        List<BLossProp> lossProps = bLossPropDao.selectBySettlementNo(settlementNo);
        map.put("lossProps",lossProps);

        List<BLossCharge> lossCharges = bLossChargeDao.selectBySettlementNo(settlementNo);
        map.put("lossCharges",lossCharges);

        List<BLossPerson> lossPersonList = bLossPersonDao.selectBySettlementNo(settlementNo);
        for (BLossPerson lossPerson : lossPersonList) {
            List<BLossPersonFee> feeList = bLossPersonFeeDao.selectByLossPersonId(lossPerson.getId());
            lossPerson.setLossPersonFee(feeList);
        }
        map.put("lossPersonList",lossPersonList);

        return map;
    }

    @Override
    public void buildContent(SettlementFormVo params) {
        BSettlementMain settlementMain = params.getSettlementMain();
        List<BLossProp> lossProps = params.getLossProps();
        List<BLossCharge> lossCharges = params.getLossCharges();
        List<BLossPerson> lossPersonList = params.getLossPersonList();
        StringBuffer content = new StringBuffer();
        content.append("*****??????????????????*****\n");
        content.append("???????????????\n");
        //????????????????????????
        if (!"F".equals(settlementMain.getSettlementType())) {
            BigDecimal sumls = BigDecimal.ZERO;
            BigDecimal sumpf = BigDecimal.ZERO;
            for (BLossProp lossProp : lossProps) {
                content.append("????????????????????????"+lossProp.getClauseName()+"-"+lossProp.getItemName()+"\n");
                content.append("??????????????????????????????*????????????-????????????*???1-????????????= ????????????\n");
                BigDecimal sumAmt = (lossProp.getSumLossChecked().multiply(lossProp.getClaimRate().divide(new BigDecimal(100))).subtract(lossProp.getDeductAddAmt())).multiply((new BigDecimal(1).subtract(lossProp.getDeductAddRate().divide(new BigDecimal(100)))));
                content.append("    ???"+lossProp.getSumLossChecked()+"*"+lossProp.getClaimRate()+"%-"+lossProp.getDeductAddAmt()+"???*???1-"+lossProp.getDeductAddRate()+"%???="+sumAmt.setScale(2,BigDecimal.ROUND_HALF_UP)+"\n");
                lossProp.setSumAmt(sumAmt);
                sumls = sumls.add(sumAmt);
                if (lossProp.getSumRealPay() == null) {
                    lossProp.setSumRealPay(sumAmt);
                }
                sumpf = sumpf.add(lossProp.getSumRealPay());
                content.append("?????????????????????????????????????????? "+sumAmt+" ??????????????? "+lossProp.getSumRealPay()+"\n\n");
            }

            for (BLossPerson lossPerson : lossPersonList) {
                for (BLossPersonFee lossPersonFee : lossPerson.getLossPersonFee()) {
                    String feeTypeName = "";
                    switch (lossPersonFee.getFeeTypeCode()) {
                        case "1": feeTypeName = "?????????";break;
                        case "2": feeTypeName = "???????????????";break;
                        case "3": feeTypeName = "???????????????";break;
                        case "4": feeTypeName = "?????????";break;
                        case "5": feeTypeName = "?????????";break;
                        case "6": feeTypeName = "??????";break;
                        default: feeTypeName = "??????";
                    }
                    content.append("????????????????????????"+lossPerson.getPersonName()+"-"+lossPersonFee.getClauseName()+"-"+lossPersonFee.getItemName()+"-"+feeTypeName+"\n");
                    content.append("??????????????????????????????*????????????-????????????*???1-????????????= ????????????\n");
                    BigDecimal sumAmt = (lossPersonFee.getSumLossChecked().multiply(lossPersonFee.getClaimRate().divide(new BigDecimal(100))).subtract(lossPersonFee.getDeductAddAmt())).multiply((new BigDecimal(1).subtract(lossPersonFee.getDeductAddRate().divide(new BigDecimal(100)))));
                    content.append("    ???"+lossPersonFee.getSumLossChecked()+"*"+lossPersonFee.getClaimRate()+"%-"+lossPersonFee.getDeductAddAmt()+"???*???1-"+lossPersonFee.getDeductAddRate()+"%???="+sumAmt.setScale(2,BigDecimal.ROUND_HALF_UP)+"\n");
                    lossPersonFee.setSumAmt(sumAmt);
                    sumls = sumls.add(sumAmt);
                    if (lossPersonFee.getSumRealPay() == null) {
                        lossPersonFee.setSumRealPay(sumAmt);
                    }
                    sumpf = sumpf.add(lossPersonFee.getSumRealPay());
                    content.append("?????????????????????????????????????????? "+sumAmt+" ??????????????? "+lossPersonFee.getSumRealPay()+"\n\n");
                }
            }

            content.append("???????????????????????????????????? "+sumls+" ??????????????? "+sumpf);
            settlementMain.setSumAmt(sumls);
            BigDecimal preAmt = BigDecimal.ZERO;
            //??????????????????
            if ("P".equals(settlementMain.getSettlementType())) {
                List<BSettlementMain> ySettlement = bSettlementMainDao.selectUndwrtSettlement(settlementMain.getReportNo(),SettlementTypeEnum.Y.getCode());
                List<BSettlementMain> pSettlement = bSettlementMainDao.selectUndwrtSettlement(settlementMain.getReportNo(),SettlementTypeEnum.P.getCode());
                //??????????????????????????????????????????
                if ((pSettlement == null || pSettlement.size() == 0) && (ySettlement != null && ySettlement.size() > 0)) {
                    preAmt = ySettlement.get(0).getSumRealPay();
                    settlementMain.setSumPreAmt(preAmt);
                    sumpf = sumpf.subtract(preAmt);
                    content.append("\n\n?????????????????????????????????????????? "+preAmt);
                }
            }

            settlementMain.setSumPreAmt(preAmt);
            settlementMain.setSumPaidAmt(sumls);
            settlementMain.setSumRealPay(sumpf);
            settlementMain.setPayStatus("0");
        } else {
            BigDecimal sumls = BigDecimal.ZERO;
            BigDecimal sumpf = BigDecimal.ZERO;
            for (BLossCharge lossCharge : lossCharges) {
                String costName = "";
                switch (lossCharge.getCostType()) {
                    case "1":costName="?????????";break;
                    case "2":costName="?????????";break;
                    case "3":costName="?????????";break;
                    case "4":costName="?????????";break;
                    case "5":costName="?????????";break;
                    case "6":costName="??????";break;
                }
                content.append("???????????????"+lossCharge.getClauseName()+"-"+costName+"\n");
                content.append("???????????????"+lossCharge.getSumRealPay()+"\n\n");
                sumls = sumls.add(lossCharge.getSumRealPay());
                sumpf = sumpf.add(lossCharge.getSumRealPay());
            }
            content.append("???????????????????????????????????? "+sumls+" ??????????????? "+sumpf);
            settlementMain.setSumFee(sumls);
            settlementMain.setSumRealPay(sumpf);
            settlementMain.setPayStatus("0");
        }
        settlementMain.setContent(content.toString());
    }

    @Override
    public void saveSettlement(SettlementFormVo vo) {
        BSettlementMain settlementMain = vo.getSettlementMain();
        String reportNo = settlementMain.getReportNo();
        String settlementNo = settlementMain.getSettlementNo();
        BSettlementMain po = bSettlementMainDao.selectById(settlementMain.getSettlementNo());
        if (po != null) {
            bSettlementMainDao.updateById(settlementMain);
        } else {
            bSettlementMainDao.insert(settlementMain);
        }

        //????????????
        if (SettlementTypeEnum.F.getCode().equals(settlementMain.getSettlementType())) {
            List<BLossCharge> chargeList = vo.getLossCharges();
            bLossChargeDao.deleteBySettlementNo(reportNo,settlementNo);
            for (BLossCharge charge : chargeList) {
                bLossChargeDao.insert(charge);
            }
        } else {
            List<BLossProp> propList = vo.getLossProps();
            bLossPropDao.deleteBySettlementNo(reportNo,settlementNo);
            for (BLossProp prop : propList) {
                bLossPropDao.insert(prop);
            }

            List<BLossPerson> personList = vo.getLossPersonList();
            bLossPersonDao.deleteBySettlementNo(reportNo,settlementNo);
            bLossPersonFeeDao.deleteBySettlementNo(reportNo,settlementNo);
            for (BLossPerson person : personList) {
                bLossPersonDao.insert(person);
                for (BLossPersonFee personFee : person.getLossPersonFee()) {
                    personFee.setLossPersonId(person.getId());
                    bLossPersonFeeDao.insert(personFee);
                }
            }
        }

        //????????????
        List<BPayment> paymentList = vo.getPaymentList();
        bPaymentDao.deleteBySettlementNo(reportNo,settlementNo);
        for (BPayment payment : paymentList) {
            bPaymentDao.insert(payment);
        }
    }

    @Override
    public void submitSettlement(SettlementFormVo vo) {
        vo.getSettlementMain().setUnderwriteFlag(UnderwriteFlagEnum.DOING.getCode());
        vo.getSettlementMain().setSettlementDate(new Date());
        saveSettlement(vo);
        generateWorkflow(vo.getSettlementMain());
    }

    @Override
    public void cancelSettlement(String settlementNo) {
        BSettlementMain bSettlementMain = bSettlementMainDao.selectById(settlementNo);
        if (bSettlementMain != null) {
            bSettlementMain.setUnderwriteFlag(UnderwriteFlagEnum.CANCEL.getCode());
        }
        bSettlementMainDao.updateById(bSettlementMain);
    }

    @Override
    public void undwrt(BApprove approve) {
        BSettlementMain settlementMain = bSettlementMainDao.selectById(approve.getSettlementNo());
        BClaimMain claimMain = bClaimMainDao.selectByReportNo(approve.getReportNo());
        BReportMain reportMain = bReportMainDao.selectByReportNo(approve.getReportNo());
        //?????????????????????????????????
        BWorkflow preWorkflow = workflowDao.selectNodeByReportNo(settlementMain.getReportNo(),TaskTypeEnum.Undwrt.getCode()+"_"+approve.getApproveNode(),approve.getApproveNo().toString());
        preWorkflow.setEndTime(new Date());
        if ("1".equals(approve.getApproveFlag())) {
            preWorkflow.setTaskStatus(TaskStatusEnum.DONE.getCode());
        } else {
            preWorkflow.setTaskStatus(TaskStatusEnum.BACK.getCode());
        }
        workflowDao.updateById(preWorkflow);

        //????????????
        if ("1".equals(approve.getApproveFlag())) {
            Boolean flag = true;
            BigDecimal sumRealPay = settlementMain.getSumRealPay();
            //???????????????????????????
            SProcessMain processMain = sProcessMainDao.selectLastProcess(TaskTypeEnum.Undwrt.getCode(),settlementMain.getInsuranceCode());
            if (processMain == null) {
                //?????????????????????????????????????????????????????????
                processMain = sProcessMainDao.selectCommonProcess(TaskTypeEnum.Undwrt.getCode());
            }

            if (processMain != null) {
                SProcessNode node = sProcessNodeDao.selectByProcessIdAndNodeId(processMain.getId(),approve.getApproveNode());
                BigDecimal amount = node.getAmount();
                //????????????????????????????????????
                if (amount != null && sumRealPay.compareTo(amount) > 0) {
                    Integer nextNodeId = Integer.valueOf(approve.getApproveNode()) + 1;
                    SProcessNode nextNode = sProcessNodeDao.selectByProcessIdAndNodeId(processMain.getId(),nextNodeId.toString());

                    //??????????????????
                    if (nextNode != null) {
                        BApprove bApprove = new BApprove();
                        bApprove.setPreApproveNo(approve.getApproveNo());
                        bApprove.setReportNo(settlementMain.getReportNo());
                        bApprove.setClaimNo(settlementMain.getClaimNo());
                        bApprove.setSettlementNo(settlementMain.getSettlementNo());
                        bApprove.setApproveNode(nextNodeId.toString());
                        bApprove.setApproveTypeCode(TaskTypeEnum.Undwrt.getCode());
                        bApprove.setApproveTypeName(TaskTypeEnum.Undwrt.getName());
                        Integer approveNo = approveService.createApprove(bApprove);

                        //???????????????
                        BWorkflow nextWorkflow = new BWorkflow();
                        nextWorkflow.setPrevTaskId(preWorkflow.getTaskId());
                        nextWorkflow.setTaskType(TaskTypeEnum.Undwrt.getCode()+"_"+nextNodeId.toString());
                        nextWorkflow.setCreator(approve.getApproverCode());
                        nextWorkflow.setHandler(nextNode.getHandler());
                        nextWorkflow.setStartTime(new Date());
                        nextWorkflow.setTaskStatus(TaskStatusEnum.DOING.getCode());
                        nextWorkflow.setComCode(settlementMain.getComCode());
                        nextWorkflow.setBusinessKey(approveNo.toString());
                        nextWorkflow.setReportNo(settlementMain.getReportNo());
                        nextWorkflow.setPolicyNo(settlementMain.getPolicyNo());
                        nextWorkflow.setInsurance(settlementMain.getInsuranceCode());
                        nextWorkflow.setRemark(approve.getApproverCode()+" ????????????");
                        workflowDao.insert(nextWorkflow);
                        flag = false;
                    }
                }
            }

            //????????????????????????????????????
            if (flag) {
                settlementMain.setUnderwriteFlag(UnderwriteFlagEnum.DONE.getCode());
                settlementMain.setUnderwriteDate(new Date());
                bSettlementMainDao.updateById(settlementMain);
                reportMain.setCaseFlag(CaseFlagEnum.Settlement.getCode());
                bReportMainDao.updateById(reportMain);
                if (!"F".equals(settlementMain.getSettlementType())) {
                    BigDecimal sumClaim = claimMain.getSumClaim() != null ? claimMain.getSumClaim() : BigDecimal.ZERO;
                    BigDecimal sumPaid = claimMain.getSumPaid() != null ? claimMain.getSumPaid() : BigDecimal.ZERO;
                    sumPaid = sumPaid.add(sumRealPay);
                    sumClaim = sumClaim.add(sumRealPay);
                    claimMain.setSumPaid(sumPaid);
                    claimMain.setSumClaim(sumClaim);
                    bClaimMainDao.updateById(claimMain);
                } else {
                    BigDecimal sumClaim = claimMain.getSumClaim() != null ? claimMain.getSumClaim() : BigDecimal.ZERO;
                    BigDecimal sumFee = claimMain.getSumFee() != null ? claimMain.getSumFee() : BigDecimal.ZERO;
                    sumFee = sumFee.add(sumRealPay);
                    sumClaim = sumClaim.add(sumRealPay);
                    claimMain.setSumFee(sumFee);
                    claimMain.setSumClaim(sumClaim);
                    bClaimMainDao.updateById(claimMain);
                }

                //????????????????????????
                if("1".equals(settlementMain.getEndCaseFlag())) {
                    Map map = endCaseService.applyEndCase(settlementMain.getReportNo());
                    if ("1".equals(map.get("status"))) {
                        endCaseService.autoEndCase(claimMain);
                    }
                }
            }
        } else {
            //??????
            settlementMain.setUnderwriteFlag(UnderwriteFlagEnum.INITORBACK.getCode());
            bSettlementMainDao.updateById(settlementMain);
        }
    }

    @Override
    public Map initSettlementForUndwrt(String reportNo, String businessKey) {
        Map map = new HashMap();
        BApprove bApprove = bApproveDao.selectById(businessKey);
        String settlementNo = bApprove.getSettlementNo();

        map = initSettlementInfo(reportNo,settlementNo);

        List<BPayment> paymentList = bPaymentDao.selectBySettlementNo(settlementNo);
        for (BPayment payment : paymentList) {
            BPayee payee = bPayeeDao.selectById(payment.getPayeeId());
            payment.setPayeeInfo(payee);
        }
        map.put("paymentList",paymentList);

        return map;
    }

    @Override
    public Map checkSettlementForAdd(String reportNo,String settlementType) {
        Map map = new HashMap();
        map.put("status","1");
        if (SettlementTypeEnum.Y.getCode().equals(settlementType)) {
            List<BSettlementMain> settlementY = bSettlementMainDao.selectBySettlementType(reportNo,SettlementTypeEnum.Y.getCode());
            for (BSettlementMain settlementMain : settlementY) {
                if (!UnderwriteFlagEnum.CANCEL.getCode().equals(settlementMain.getUnderwriteFlag())) {
                    map.put("status","0");
                    if (UnderwriteFlagEnum.DONE.getCode().equals(settlementMain.getUnderwriteFlag())) {
                        map.put("msg","???????????????????????????????????????????????????");
                    } else {
                        map.put("msg","???????????????????????????????????????????????????");
                    }
                    return map;
                }
            }

            List<BSettlementMain> settlementP = bSettlementMainDao.selectBySettlementType(reportNo,SettlementTypeEnum.P.getCode());
            for (BSettlementMain settlementMain : settlementP) {
                if (!UnderwriteFlagEnum.CANCEL.getCode().equals(settlementMain.getUnderwriteFlag())) {
                    map.put("status","0");
                    if (UnderwriteFlagEnum.DONE.getCode().equals(settlementMain.getUnderwriteFlag())) {
                        map.put("msg","???????????????????????????????????????????????????");
                    } else {
                        map.put("msg","???????????????????????????????????????????????????");
                    }
                    return map;
                }
            }
        }

        if (SettlementTypeEnum.P.getCode().equals(settlementType)) {
            List<BSettlementMain> settlementP = bSettlementMainDao.selectBySettlementType(reportNo,SettlementTypeEnum.P.getCode());
            for (BSettlementMain settlementMain : settlementP) {
                if (UnderwriteFlagEnum.DOING.getCode().equals(settlementMain.getUnderwriteFlag()) ||
                        UnderwriteFlagEnum.INITORBACK.getCode().equals(settlementMain.getUnderwriteFlag())) {
                    map.put("status","0");
                    map.put("msg","???????????????????????????????????????????????????");
                    return map;
                }
            }
        }

        List<BApprove> approves = approveService.selectEndCaseDoing(reportNo);
        if (approves != null && approves.size() > 0) {
            map.put("status","0");
            map.put("msg","???????????????????????????????????????????????????");
            return map;
        }

        BReportMain bReportMain = bReportMainDao.selectByReportNo(reportNo);
        if ("04".equals(bReportMain.getCaseFlag())) {
            map.put("status","0");
            map.put("msg","???????????????????????????????????????");
            return map;
        }
        return map;
    }

    @Override
    public Map checkSettlementForSave(String reportNo,String settlementType,String settlementNo,String sumRealPay) {
        Map map = new HashMap();
        map.put("status","1");
        if (SettlementTypeEnum.Y.getCode().equals(settlementType)) {
            List<BSettlementMain> settlementY = bSettlementMainDao.selectBySettlementType(reportNo,SettlementTypeEnum.Y.getCode());
            for (BSettlementMain settlementMain : settlementY) {
                if (!UnderwriteFlagEnum.CANCEL.getCode().equals(settlementMain.getUnderwriteFlag()) && !settlementMain.getSettlementNo().equals(settlementNo)) {
                    map.put("status","0");
                    if (UnderwriteFlagEnum.DONE.getCode().equals(settlementMain.getUnderwriteFlag())) {
                        map.put("msg","???????????????????????????????????????????????????");
                    } else {
                        map.put("msg","???????????????????????????????????????????????????");
                    }
                    return map;
                }
            }

            List<BSettlementMain> settlementP = bSettlementMainDao.selectBySettlementType(reportNo,SettlementTypeEnum.P.getCode());
            for (BSettlementMain settlementMain : settlementP) {
                if (!UnderwriteFlagEnum.CANCEL.getCode().equals(settlementMain.getUnderwriteFlag())) {
                    map.put("status","0");
                    if (UnderwriteFlagEnum.DONE.getCode().equals(settlementMain.getUnderwriteFlag())) {
                        map.put("msg","???????????????????????????????????????????????????");
                    } else {
                        map.put("msg","???????????????????????????????????????????????????");
                    }
                    return map;
                }
            }

            BClaimMain claimMain = bClaimMainDao.selectByReportNo(reportNo);
            BigDecimal sumDefloss = claimMain.getSumDefloss().divide(new BigDecimal(2));
            if (new BigDecimal(sumRealPay).compareTo(sumDefloss) > 0) {
                map.put("status","0");
                map.put("msg","?????????50%??????????????????????????????");
                return map;
            }
        }

        if (SettlementTypeEnum.P.getCode().equals(settlementType)) {
            List<BSettlementMain> settlementP = bSettlementMainDao.selectBySettlementType(reportNo,SettlementTypeEnum.P.getCode());
            for (BSettlementMain settlementMain : settlementP) {
                if ((UnderwriteFlagEnum.DOING.getCode().equals(settlementMain.getUnderwriteFlag()) ||
                        UnderwriteFlagEnum.INITORBACK.getCode().equals(settlementMain.getUnderwriteFlag())) &&  !settlementMain.getSettlementNo().equals(settlementNo)) {
                    map.put("status","0");
                    map.put("msg","???????????????????????????????????????????????????");
                    return map;
                }
            }
        }

        List<BApprove> approves = approveService.selectEndCaseDoing(reportNo);
        if (approves != null && approves.size() > 0) {
            map.put("status","0");
            map.put("msg","???????????????????????????????????????????????????");
            return map;
        }

        BReportMain bReportMain = bReportMainDao.selectByReportNo(reportNo);
        if ("04".equals(bReportMain.getCaseFlag())) {
            map.put("status","0");
            map.put("msg","???????????????????????????????????????");
            return map;
        }
        return map;
    }

    @Override
    public Map checkSettlementForSubmit(SettlementFormVo params) {
        Map map = new HashMap();
        map.put("status","1");

        BSettlementMain bSettlementMain = params.getSettlementMain();
        String reportNo = bSettlementMain.getReportNo();
        if (!SettlementTypeEnum.F.getCode().equals(bSettlementMain.getSettlementType())) {
            List<BClaimClause> clauseList = bClaimClauseDao.selectByReportNoAndFeeType(reportNo,"P");
            for (BClaimClause clause : clauseList) {
                BigDecimal amount = clause.getAmount();
                BigDecimal sumHasPaidProp = bLossPropDao.getSumHasPaid(reportNo,clause.getClauseCode(),clause.getItemCode());
                BigDecimal sumHasPaidPerson = bLossPersonFeeDao.getSumHasPaid(reportNo,clause.getClauseCode(),clause.getItemCode());
                BigDecimal propPay = BigDecimal.ZERO;
                if (params.getLossProps() != null && params.getLossProps().size() > 0) {
                    List<BLossProp> propList = params.getLossProps().stream()
                            .filter(e -> e.getClauseCode().equals(clause.getClauseCode()) && e.getItemCode().equals(clause.getItemCode()))
                            .collect(Collectors.toList());
                    for (BLossProp prop : propList) {
                        propPay = propPay.add(prop.getSumRealPay());
                    }
                }

                BigDecimal personPay = BigDecimal.ZERO;
                List<BLossPerson> personList = params.getLossPersonList();
                for (BLossPerson person : personList) {
                    if (person.getLossPersonFee() != null && person.getLossPersonFee().size() > 0) {
                        List<BLossPersonFee> feeList = person.getLossPersonFee().stream()
                                .filter(e -> e.getClauseCode().equals(clause.getClauseCode()) && e.getItemCode().equals(clause.getItemCode()))
                                .collect(Collectors.toList());
                        for (BLossPersonFee fee : feeList) {
                            personPay = personPay.add(fee.getSumRealPay());
                        }
                    }
                }
                BigDecimal sumPaid = sumHasPaidProp.add(sumHasPaidPerson).add(propPay).add(personPay);
                if (sumPaid.compareTo(amount) > 0) {
                    map.put("status","0");
                    map.put("msg","?????? "+clause.getClauseName()+"-"+clause.getItemName()+" ????????????????????????");
                    return map;
                }
            }
            /*for (BLossProp prop : list) {
                BClaimClause clause = bClaimClauseDao.selectByClauseCodeAndItemCode(reportNo,prop.getClauseCode(),prop.getItemCode());
                BigDecimal amount = clause.getAmount();
                BigDecimal sumHasPaidProp = bLossPropDao.getSumHasPaid(reportNo,prop.getClauseCode(),prop.getItemCode());
                BigDecimal sumPaid = sumHasPaidProp.add(prop.getSumRealPay());
                if (sumPaid.compareTo(amount) > 0) {
                    map.put("status","0");
                    map.put("msg","?????? "+clause.getClauseName()+"-"+clause.getItemName()+" ????????????????????????");
                    return map;
                }
            }*/
            BPolicyMain bPolicyMain = bPolicyMainDao.selectByReportNo(reportNo);
            BigDecimal policyAmount = bPolicyMain.getSumAmount();
            BigDecimal sumHasPaid = bSettlementMainDao.getSumHasPaid(reportNo);
            BigDecimal sumRealPaid = sumHasPaid.add(bSettlementMain.getSumRealPay());
            if (sumRealPaid.compareTo(policyAmount) > 0) {
                map.put("status","0");
                map.put("msg","?????????????????????????????????????????????");
                return map;
            }
        }

        return map;
    }

    private void generateWorkflow(BSettlementMain settlementMain) {
        String reportNo = settlementMain.getReportNo();
        SUser sUser = CommonUtil.getLoginUser();
        BWorkflow caseMain = workflowDao.selectCaseMain(reportNo);

        //???????????????????????????
        String handler = processService.getHandler(TaskTypeEnum.Undwrt,settlementMain.getInsuranceCode());

        BWorkflow undwrt = new BWorkflow();
        undwrt.setPrevTaskId(caseMain.getTaskId());
        undwrt.setTaskType(TaskTypeEnum.Undwrt.getCode()+"_1");
        undwrt.setCreator(sUser.getUserCode());
        if (StringUtils.isNotBlank(handler)) {
            //?????????????????????????????????????????????????????????
            undwrt.setHandler(handler);
            undwrt.setStartTime(new Date());
            undwrt.setTaskStatus(TaskStatusEnum.DOING.getCode());
        } else {
            undwrt.setTaskStatus(TaskStatusEnum.UNDO.getCode());
        }
        undwrt.setComCode(settlementMain.getComCode());
        //????????????????????????
        BApprove bApprove = new BApprove();
        bApprove.setReportNo(reportNo);
        bApprove.setClaimNo(settlementMain.getClaimNo());
        bApprove.setSettlementNo(settlementMain.getSettlementNo());
        bApprove.setApproveNode("1");
        bApprove.setApproveTypeCode(TaskTypeEnum.Undwrt.getCode());
        bApprove.setApproveTypeName(TaskTypeEnum.Undwrt.getName());
        Integer approveNo = approveService.createApprove(bApprove);
        undwrt.setBusinessKey(approveNo.toString());
        undwrt.setReportNo(settlementMain.getReportNo());
        undwrt.setPolicyNo(settlementMain.getPolicyNo());
        undwrt.setInsurance(settlementMain.getInsuranceCode());
        undwrt.setRemark(sUser.getUserName()+" ??????????????????");
        workflowDao.insert(undwrt);
    }

    private void getClaimKindP(String reportNo, Map map) {
        List<BClaimClause> claimKindP = bClaimClauseDao.selectByReportNoAndFeeType(reportNo, SettlementTypeEnum.P.getCode());
        map.put("claimKindP",claimKindP);
    }

    private void getClaimKindF(String reportNo, Map map) {
        List<BClaimClause> claimKindF = bClaimClauseDao.selectByReportNoAndFeeType(reportNo,SettlementTypeEnum.F.getCode());
        map.put("claimKindF",claimKindF);
    }
}
