package com.claim.server.endcase.service.impl;

import com.claim.server.approve.dao.BApproveDao;
import com.claim.server.approve.po.BApprove;
import com.claim.server.approve.service.ApproveService;
import com.claim.server.claim.dao.BClaimMainDao;
import com.claim.server.claim.po.BClaimMain;
import com.claim.server.common.po.SUser;
import com.claim.server.endcase.dao.BEndCaseDao;
import com.claim.server.endcase.po.BEndCase;
import com.claim.server.endcase.service.EndCaseService;
import com.claim.server.endcase.vo.EndCaseInfo;
import com.claim.server.policy.dao.BPolicyMainDao;
import com.claim.server.policy.po.BPolicyMain;
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
public class EndCaseServiceImpl implements EndCaseService {

    @Autowired
    private BSettlementMainDao bSettlementMainDao;

    @Autowired
    private ApproveService approveService;

    @Autowired
    private BClaimMainDao bClaimMainDao;

    @Autowired
    private BReportMainDao bReportMainDao;

    @Autowired
    private BWorkflowDao workflowDao;

    @Autowired
    private ProcessService processService;

    @Autowired
    private BEndCaseDao bEndCaseDao;

    @Autowired
    private BApproveDao bApproveDao;

    @Autowired
    private BPolicyMainDao bPolicyMainDao;

    @Autowired
    private SProcessMainDao sProcessMainDao;

    @Autowired
    private SProcessNodeDao sProcessNodeDao;

    @Override
    public Map applyEndCase(String reportNo) {
        Map map = new HashMap();
        map.put("status","1");
        List<BSettlementMain> list = bSettlementMainDao.selectByReportNo(reportNo);
        for (BSettlementMain bSettlementMain : list) {
            if (UnderwriteFlagEnum.INITORBACK.getCode().equals(bSettlementMain.getUnderwriteFlag())) {
                map.put("status","0");
                map.put("msg","存在处理中的理算书，不能结案");
                return map;
            }

            if (UnderwriteFlagEnum.DOING.getCode().equals(bSettlementMain.getUnderwriteFlag())) {
                map.put("status","0");
                map.put("msg","存在审核中的理算书，不能结案");
                return map;
            }
        }

        List<BApprove> approves = approveService.selectEndCaseDoing(reportNo);
        if (approves != null && approves.size() > 0) {
            map.put("status","0");
            map.put("msg","存在审核中的结案申请，不能重复申请");
            return map;
        }

        BClaimMain bClaimMain = bClaimMainDao.selectByReportNo(reportNo);
        BEndCase bEndCase = generateCaseNo(bClaimMain);
        generateEndCase(reportNo,bEndCase.getId());
        return map;
    }

    @Override
    public EndCaseInfo initEndCaseInfo(String reportNo,String businessKey) {
        EndCaseInfo endCaseInfo = new EndCaseInfo();
        BApprove bApprove = bApproveDao.selectById(Integer.valueOf(businessKey));
        Integer endCaseId = bApprove.getEndCaseId();
        BEndCase endCase = bEndCaseDao.selectById(endCaseId);
        BClaimMain bClaimMain = bClaimMainDao.selectByReportNo(reportNo);
        BReportMain bReportMain = bReportMainDao.selectByReportNo(reportNo);
        BPolicyMain bPolicyMain = bPolicyMainDao.selectByReportNo(reportNo);
        endCaseInfo.setReportNo(endCase.getReportNo());
        endCaseInfo.setClaimNo(endCase.getClaimNo());
        endCaseInfo.setCaseNo(endCase.getCaseNo());
        endCaseInfo.setClaimTime(bClaimMain.getClaimTime());
        endCaseInfo.setComName(bClaimMain.getComName());
        endCaseInfo.setEndCaseTime(endCase.getEndCaseTime());
        endCaseInfo.setDamageTime(bReportMain.getDamageTime());
        endCaseInfo.setDamageCode(bReportMain.getDamageCode());
        String[] str = new String[]{
                bReportMain.getProvince(),bReportMain.getCity(),bReportMain.getCountyCode(),bReportMain.getDamageAddress()
        };
        endCaseInfo.setDamageAddress(String.join("-",str));
        endCaseInfo.setSumClaim(bClaimMain.getSumClaim());
        endCaseInfo.setAmount(bPolicyMain.getSumAmount());
        endCaseInfo.setInsuredName(bPolicyMain.getInsuredName());
        endCaseInfo.setInsuranceName(bPolicyMain.getInsuranceName());
        endCaseInfo.setHandler(endCase.getHandlerName());
        endCaseInfo.setEndCaser(endCase.getEndCaserName());
        return endCaseInfo;
    }

    @Override
    public void endCase(BApprove approve) {
        SUser user = CommonUtil.getLoginUser();
        BEndCase endCase = bEndCaseDao.selectById(approve.getEndCaseId());
        BClaimMain claimMain = bClaimMainDao.selectByReportNo(approve.getReportNo());
        BReportMain reportMain = bReportMainDao.selectByReportNo(approve.getReportNo());
        //获取当前任务并设置完成
        BWorkflow preWorkflow = workflowDao.selectNodeByReportNo(endCase.getReportNo(),TaskTypeEnum.EndCase.getCode()+"_"+approve.getApproveNode(),approve.getApproveNo().toString());
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
            BigDecimal sumRealPay = endCase.getSumClaim();
            //获取配置的审核流程
            SProcessMain processMain = sProcessMainDao.selectLastProcess(TaskTypeEnum.EndCase.getCode(),endCase.getInsuranceCode());
            if (processMain == null) {
                //不存在针对险种的特殊配置则获取通用配置
                processMain = sProcessMainDao.selectCommonProcess(TaskTypeEnum.EndCase.getCode());
            }

            if (processMain != null) {
                SProcessNode node = sProcessNodeDao.selectByProcessIdAndNodeId(processMain.getId(),approve.getApproveNode());
                BigDecimal amount = node.getAmount();
                //不在权限范围内提交上一级
                if (amount != null && sumRealPay.compareTo(amount) > 0) {
                    Integer nextNodeId = Integer.valueOf(approve.getApproveNode()) + 1;
                    SProcessNode nextNode = sProcessNodeDao.selectByProcessIdAndNodeId(processMain.getId(),nextNodeId.toString());

                    //存在下一节点
                    if (nextNode != null) {
                        BApprove bApprove = new BApprove();
                        bApprove.setPreApproveNo(approve.getApproveNo());
                        bApprove.setReportNo(endCase.getReportNo());
                        bApprove.setClaimNo(endCase.getClaimNo());
                        bApprove.setEndCaseId(endCase.getId());
                        bApprove.setApproveNode(nextNodeId.toString());
                        bApprove.setApproveTypeCode(TaskTypeEnum.EndCase.getCode());
                        bApprove.setApproveTypeName(TaskTypeEnum.EndCase.getName());
                        Integer approveNo = approveService.createApprove(bApprove);

                        //创建新任务
                        BWorkflow nextWorkflow = new BWorkflow();
                        nextWorkflow.setPrevTaskId(preWorkflow.getTaskId());
                        nextWorkflow.setTaskType(TaskTypeEnum.EndCase.getCode()+"_"+nextNodeId.toString());
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

            //通过更新报案和立案信息
            if (flag) {
                reportMain.setCaseFlag(CaseFlagEnum.EndCase.getCode());
                bReportMainDao.updateById(reportMain);
                claimMain.setCaseNo(endCase.getCaseNo());
                claimMain.setEndCaseTime(new Date());
                bClaimMainDao.updateById(claimMain);
                endCase.setEndCaseTime(new Date());
                endCase.setEndCaserCode(user.getUserCode());
                endCase.setEndCaserName(user.getUserName());
                BWorkflow caseMain = workflowDao.selectCaseMain(reportMain.getReportNo());
                caseMain.setEndTime(new Date());
                caseMain.setTaskStatus(TaskStatusEnum.DONE.getCode());
                workflowDao.updateById(caseMain);
            }
        }
    }

    private BEndCase generateCaseNo(BClaimMain bClaimMain) {
        SUser sUser = CommonUtil.getLoginUser();
        String endCaseTime = CommonUtil.formatDateTime(new Date(),"yyyyMMddHHmmss");
        String insuranceCode = bClaimMain.getInsuranceCode();
        String caseNo = "E"+insuranceCode+endCaseTime;
        BEndCase endCase = new BEndCase();
        endCase.setReportNo(bClaimMain.getReportNo());
        endCase.setClaimNo(bClaimMain.getClaimNo());
        endCase.setCaseNo(caseNo);
        endCase.setInsuranceCode(bClaimMain.getInsuranceCode());
        endCase.setInsuranceName(bClaimMain.getInsuranceName());
        endCase.setHandlerCode(sUser.getUserCode());
        endCase.setHandlerName(sUser.getUserName());
        endCase.setSumClaim(bClaimMain.getSumClaim());
        bEndCaseDao.insert(endCase);

        return endCase;
    }

    private void generateEndCase(String reportNo,Integer endCaseId) {
        BClaimMain claimMain = bClaimMainDao.selectByReportNo(reportNo);
        BWorkflow caseMain = workflowDao.selectCaseMain(reportNo);
        SUser sUser = CommonUtil.getLoginUser();

        //获取配置的审核流程
        String handler = processService.getHandler(TaskTypeEnum.EndCase,claimMain.getInsuranceCode());

        BWorkflow endCase = new BWorkflow();
        endCase.setPrevTaskId(caseMain.getTaskId());
        endCase.setTaskType(TaskTypeEnum.EndCase.getCode()+"_1");
        endCase.setCreator(sUser.getUserCode());
        if (StringUtils.isNotBlank(handler)) {
            //如果存在受理人则默认接收并给与开始时间
            endCase.setHandler(handler);
            endCase.setStartTime(new Date());
            endCase.setTaskStatus(TaskStatusEnum.DOING.getCode());
        } else {
            endCase.setTaskStatus(TaskStatusEnum.UNDO.getCode());
        }
        endCase.setComCode(claimMain.getComCode());
        //创建审批节点数据
        BApprove bApprove = new BApprove();
        bApprove.setReportNo(reportNo);
        bApprove.setClaimNo(claimMain.getClaimNo());
        bApprove.setEndCaseId(endCaseId);
        bApprove.setApproveNode("1");
        bApprove.setApproveTypeCode(TaskTypeEnum.EndCase.getCode());
        bApprove.setApproveTypeName(TaskTypeEnum.EndCase.getName());
        Integer approveNo = approveService.createApprove(bApprove);
        endCase.setBusinessKey(approveNo.toString());
        endCase.setReportNo(claimMain.getReportNo());
        endCase.setPolicyNo(claimMain.getPolicyNo());
        endCase.setInsurance(claimMain.getInsuranceCode());
        endCase.setRemark(sUser.getUserName()+" 提交结案审核");
        workflowDao.insert(endCase);
    }
}
