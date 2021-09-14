package com.claim.server.claim.service.impl;

import com.claim.server.approve.po.BApprove;
import com.claim.server.approve.service.ApproveService;
import com.claim.server.claim.dao.BClaimClauseDao;
import com.claim.server.claim.dao.BClaimMainDao;
import com.claim.server.claim.po.BClaimClause;
import com.claim.server.claim.po.BClaimMain;
import com.claim.server.claim.service.ClaimService;
import com.claim.server.claim.vo.ClaimFormVo;
import com.claim.server.common.po.SUser;
import com.claim.server.policy.dao.BPolicyClauseDao;
import com.claim.server.policy.po.BPolicyClause;
import com.claim.server.process.dao.SProcessMainDao;
import com.claim.server.process.dao.SProcessNodeDao;
import com.claim.server.process.po.SProcessMain;
import com.claim.server.process.po.SProcessNode;
import com.claim.server.process.service.ProcessService;
import com.claim.server.report.dao.BReportMainDao;
import com.claim.server.report.po.BReportMain;
import com.claim.server.settlement.dao.BSettlementMainDao;
import com.claim.server.settlement.po.BSettlementMain;
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

@Service
public class ClaimServiceImpl implements ClaimService {

    @Autowired
    private BReportMainDao bReportMainDao;

    @Autowired
    private BPolicyClauseDao bPolicyClauseDao;

    @Autowired
    private BClaimMainDao bClaimMainDao;

    @Autowired
    private BClaimClauseDao bClaimClauseDao;

    @Autowired
    private BWorkflowDao workflowDao;

    @Autowired
    private SProcessMainDao sProcessMainDao;

    @Autowired
    private SProcessNodeDao sProcessNodeDao;

    @Autowired
    private ApproveService approveService;

    @Autowired
    private ProcessService processService;

    @Override
    public Map createClaimInfo(String reportNo) {
        Map map = new HashMap();
        BReportMain reportMain = bReportMainDao.selectByReportNo(reportNo);

        //生成立案主表信息
        BClaimMain claimMain = new BClaimMain();
        String policyNo = reportMain.getPolicyNo();
        claimMain.setReportNo(reportNo);
        claimMain.setPolicyNo(policyNo);
        claimMain.setComCode(reportMain.getComCode());
        claimMain.setComName(reportMain.getComName());
        claimMain.setDamageCode(reportMain.getDamageCode());
        claimMain.setDamageTime(reportMain.getDamageTime());
        claimMain.setInsuranceCode(reportMain.getInsuranceCode());
        claimMain.setInsuranceName(reportMain.getInsuranceName());
        claimMain.setInsuredCode(reportMain.getInsuredCode());
        claimMain.setInsuredName(reportMain.getInsuredName());
        claimMain.setReportLoss(reportMain.getReportLoss());
        claimMain.setRemark(reportMain.getRemark());
        map.put("claimMain",claimMain);

        //获取条款列表用于编辑修改展示
        getClause(map,policyNo);

        return map;
    }

    @Override
    public Map getClaimInfo(BClaimMain claimMain,Map map) {
        String policyNo = claimMain.getPolicyNo();
        String reportNo = claimMain.getReportNo();

        List<BClaimClause> claimKindP = bClaimClauseDao.selectByReportNoAndFeeType(reportNo,"P");
        map.put("claimKindP",claimKindP);

        List<BClaimClause> claimKindF = bClaimClauseDao.selectByReportNoAndFeeType(reportNo,"F");
        map.put("claimKindF",claimKindF);

        //获取条款列表用于编辑修改展示
        getClause(map,policyNo);

        return map;
    }

    @Override
    public void saveClaim(ClaimFormVo vo) {
        BClaimMain claimMain = vo.getClaimMain();
        if (StringUtils.isBlank(claimMain.getClaimNo())) {
            generateClaimNo(claimMain);
            bClaimMainDao.insert(claimMain);
        } else {
            bClaimMainDao.updateById(claimMain);
        }

        //先删后插
        String claimNo = claimMain.getClaimNo();
        bClaimClauseDao.deleteByClaimNo(claimNo);

        List<BClaimClause> claimKindP = vo.getClaimKindP();
        for (BClaimClause kindP : claimKindP) {
            kindP.setClaimNo(claimNo);
            bClaimClauseDao.insert(kindP);
        }
        List<BClaimClause> claimKindF = vo.getClaimKindF();
        for (BClaimClause kindF : claimKindF) {
            kindF.setClaimNo(claimNo);
            bClaimClauseDao.insert(kindF);
        }
    }

    @Override
    public void submitClaim(ClaimFormVo vo) {
        saveClaim(vo);
        generateWorkflow(vo.getClaimMain());
    }

    @Override
    public void claimApprove(BApprove approve) {
        BClaimMain claimMain = bClaimMainDao.selectByReportNo(approve.getReportNo());
        BReportMain reportMain = bReportMainDao.selectByReportNo(approve.getReportNo());
        //获取当前任务并设置完成
        BWorkflow preWorkflow = workflowDao.selectNodeByReportNo(claimMain.getReportNo(),TaskTypeEnum.ClaimApprove.getCode()+"_"+approve.getApproveNode(),approve.getApproveNo().toString());
        preWorkflow.setEndTime(new Date());
        if ("1".equals(approve.getApproveFlag())) {
            preWorkflow.setTaskStatus(TaskStatusEnum.DONE.getCode());
        } else {
            preWorkflow.setTaskStatus(TaskStatusEnum.BACK.getCode());
        }
        workflowDao.updateById(preWorkflow);

        //选择同意
        if ("1".equals(approve.getApproveFlag())) {
            Boolean flag = true;
            BigDecimal sumDefloss = claimMain.getSumDefloss();
            //获取配置的审核流程
            SProcessMain processMain = sProcessMainDao.selectLastProcess(TaskTypeEnum.ClaimApprove.getCode(),claimMain.getInsuranceCode());
            if (processMain == null) {
                //不存在针对险种的特殊配置则获取通用配置
                processMain = sProcessMainDao.selectCommonProcess(TaskTypeEnum.ClaimApprove.getCode());
            }

            if (processMain != null) {
                SProcessNode node = sProcessNodeDao.selectByProcessIdAndNodeId(processMain.getId(),approve.getApproveNode());
                BigDecimal amount = node.getAmount();
                //不在权限范围内提交上一级
                if (amount != null && sumDefloss.compareTo(amount) > 0) {
                    Integer nextNodeId = Integer.valueOf(approve.getApproveNode()) + 1;
                    SProcessNode nextNode = sProcessNodeDao.selectByProcessIdAndNodeId(processMain.getId(),nextNodeId.toString());

                    //存在下一节点
                    if (nextNode != null) {
                        BApprove bApprove = new BApprove();
                        bApprove.setPreApproveNo(approve.getApproveNo());
                        bApprove.setReportNo(claimMain.getReportNo());
                        bApprove.setClaimNo(claimMain.getClaimNo());
                        bApprove.setApproveNode(nextNodeId.toString());
                        bApprove.setApproveTypeCode(TaskTypeEnum.ClaimApprove.getCode());
                        bApprove.setApproveTypeName(TaskTypeEnum.ClaimApprove.getName());
                        Integer approveNo = approveService.createApprove(bApprove);

                        //创建新任务
                        BWorkflow nextWorkflow = new BWorkflow();
                        nextWorkflow.setPrevTaskId(preWorkflow.getTaskId());
                        nextWorkflow.setTaskType(TaskTypeEnum.ClaimApprove.getCode()+"_"+nextNodeId.toString());
                        nextWorkflow.setCreator(approve.getApproverCode());
                        nextWorkflow.setHandler(nextNode.getHandler());
                        nextWorkflow.setStartTime(new Date());
                        nextWorkflow.setTaskStatus(TaskStatusEnum.DOING.getCode());
                        nextWorkflow.setComCode(claimMain.getComCode());
                        nextWorkflow.setBusinessKey(approveNo.toString());
                        nextWorkflow.setReportNo(claimMain.getReportNo());
                        nextWorkflow.setPolicyNo(claimMain.getPolicyNo());
                        nextWorkflow.setInsurance(claimMain.getInsuranceCode());
                        nextWorkflow.setRemark(approve.getApproverCode()+" 提交审核");
                        workflowDao.insert(nextWorkflow);
                        flag = false;
                    }
                }
            }

            //通过更新立案时间
            if (flag){
                claimMain.setClaimStatus("2");
                claimMain.setClaimTime(new Date());
                bClaimMainDao.updateById(claimMain);
                reportMain.setCaseFlag(CaseFlagEnum.Claim.getCode());
                bReportMainDao.updateById(reportMain);
            }
        } else {
            //退回
            claimMain.setClaimStatus("0");
            bClaimMainDao.updateById(claimMain);
        }
    }

    private void generateClaimNo(BClaimMain claimMain) {
        String claimNo = claimMain.getReportNo().replace("R","C");
        claimMain.setClaimNo(claimNo);
    }

    private void getClause(Map map,String policyNo) {
        //获取保单赔款条款信息
        List<BPolicyClause> clauseP = this.getClauseP(policyNo);
        map.put("clauseP",clauseP);

        //获取保单费用条款信息
        List<BPolicyClause> clauseF = this.getClauseF(policyNo);
        map.put("clauseF",clauseF);
    }

    private List<BPolicyClause> getClauseP(String policyNo) {

        List<BPolicyClause> clauseP = bPolicyClauseDao.selectClauseByPolicyNo(policyNo,"P");
        //获取赔款条款损失明细
        for (BPolicyClause clause : clauseP) {
            List<BPolicyClause> itemList = bPolicyClauseDao.selectItems(policyNo,clause.getClauseCode());
            clause.setItemList(itemList);
        }
        return clauseP;
    }

    private List<BPolicyClause> getClauseF(String policyNo) {
        List<BPolicyClause> clauseF = bPolicyClauseDao.selectClauseByPolicyNo(policyNo,"F");
        return clauseF;
    }

    private void generateWorkflow(BClaimMain claimMain) {
        String reportNo = claimMain.getReportNo();
        SUser sUser = CommonUtil.getLoginUser();
        BWorkflow caseMain = workflowDao.selectCaseMain(reportNo);
        if (TaskStatusEnum.UNDO.getCode().equals(caseMain.getTaskStatus())) {
            caseMain.setTaskStatus(TaskStatusEnum.DOING.getCode());
            caseMain.setHandler(sUser.getUserCode());
            caseMain.setStartTime(new Date());
            workflowDao.updateById(caseMain);
        }

        //获取配置的审核流程
        String handler = processService.getHandler(TaskTypeEnum.ClaimApprove,claimMain.getInsuranceCode());

        BWorkflow claim = new BWorkflow();
        claim.setPrevTaskId(caseMain.getTaskId());
        claim.setTaskType(TaskTypeEnum.ClaimApprove.getCode()+"_1");
        claim.setCreator(sUser.getUserCode());
        if (StringUtils.isNotBlank(handler)) {
            //如果存在受理人则默认接收并给与开始时间
            claim.setHandler(handler);
            claim.setStartTime(new Date());
            claim.setTaskStatus(TaskStatusEnum.DOING.getCode());
        } else {
            claim.setTaskStatus(TaskStatusEnum.UNDO.getCode());
        }
        claim.setComCode(claimMain.getComCode());
        //创建审批节点数据
        BApprove bApprove = new BApprove();
        bApprove.setReportNo(reportNo);
        bApprove.setClaimNo(claimMain.getClaimNo());
        bApprove.setApproveNode("1");
        bApprove.setApproveTypeCode(TaskTypeEnum.ClaimApprove.getCode());
        bApprove.setApproveTypeName(TaskTypeEnum.ClaimApprove.getName());
        Integer approveNo = approveService.createApprove(bApprove);
        claim.setBusinessKey(approveNo.toString());
        claim.setReportNo(claimMain.getReportNo());
        claim.setPolicyNo(claimMain.getPolicyNo());
        claim.setInsurance(claimMain.getInsuranceCode());
        claim.setRemark(sUser.getUserName()+" 提交立案审核");
        workflowDao.insert(claim);
    }
}
