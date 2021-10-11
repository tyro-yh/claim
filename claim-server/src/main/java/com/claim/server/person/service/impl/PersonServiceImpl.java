package com.claim.server.person.service.impl;

import com.claim.server.approve.dao.BApproveDao;
import com.claim.server.approve.po.BApprove;
import com.claim.server.approve.service.ApproveService;
import com.claim.server.claim.dao.BClaimMainDao;
import com.claim.server.claim.po.BClaimMain;
import com.claim.server.common.po.SUser;
import com.claim.server.person.dao.BPersonFeeDao;
import com.claim.server.person.dao.BPersonInjuredDao;
import com.claim.server.person.dao.BPersonMainDao;
import com.claim.server.person.dao.BPersonTraceDao;
import com.claim.server.person.po.BPersonFee;
import com.claim.server.person.po.BPersonInjured;
import com.claim.server.person.po.BPersonMain;
import com.claim.server.person.po.BPersonTrace;
import com.claim.server.person.service.PersonService;
import com.claim.server.person.vo.PersonFormVo;
import com.claim.server.process.dao.SProcessMainDao;
import com.claim.server.process.dao.SProcessNodeDao;
import com.claim.server.process.po.SProcessMain;
import com.claim.server.process.po.SProcessNode;
import com.claim.server.process.service.ProcessService;
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
public class PersonServiceImpl implements PersonService {

    @Autowired
    private BPersonMainDao bPersonMainDao;

    @Autowired
    private BPersonInjuredDao bPersonInjuredDao;

    @Autowired
    private BPersonTraceDao bPersonTraceDao;

    @Autowired
    private BPersonFeeDao bPersonFeeDao;

    @Autowired
    private BWorkflowDao workflowDao;

    @Autowired
    private ProcessService processService;

    @Autowired
    private BClaimMainDao bClaimMainDao;

    @Autowired
    private ApproveService approveService;

    @Autowired
    private BApproveDao bApproveDao;

    @Autowired
    private SProcessMainDao sProcessMainDao;

    @Autowired
    private SProcessNodeDao sProcessNodeDao;

    @Override
    public Map createPersonInfo(String reportNo) {
        Map map = new HashMap();
        SUser user = CommonUtil.getLoginUser();
        BPersonMain personMain = new BPersonMain();
        personMain.setReportNo(reportNo);
        personMain.setHandlerCode(user.getUserCode());
        personMain.setHandlerName(user.getUserName());
        personMain.setUnderwriteFlag(UnderwriteFlagEnum.INITORBACK.getCode());
        map.put("personMain",personMain);
        return map;
    }

    @Override
    public Map initPersonInfo(String reportNo, Integer personId) {
        Map map = new HashMap();
        BPersonMain personMain = bPersonMainDao.selectById(personId);
        map.put("personMain",personMain);

        List<BPersonInjured> injuredList = bPersonInjuredDao.selectByPersonId(personId);
        map.put("injuredList",injuredList);

        List<BPersonTrace> traceList = bPersonTraceDao.selectByPersonId(personId);
        map.put("traceList",traceList);

        List<BPersonFee> feeList = bPersonFeeDao.selectByPersonId(personId);
        map.put("feeList",feeList);

        return map;
    }

    @Override
    public void savePerson(PersonFormVo vo) {
        BPersonMain personMain = vo.getPersonMain();
        String reportNo = personMain.getReportNo();
        Integer personId = personMain.getId();
        if (personId == null) {
            bPersonMainDao.insert(personMain);
            personId = personMain.getId();
        } else {
            bPersonMainDao.updateById(personMain);
        }

        //先删后插
        bPersonInjuredDao.deleteByPersonId(reportNo,personId);
        List<BPersonInjured> injuredList = vo.getInjuredList();
        for (BPersonInjured injured : injuredList) {
            injured.setPersonId(personId);
            bPersonInjuredDao.insert(injured);
        }

        bPersonTraceDao.deleteByPersonId(reportNo,personId);
        List<BPersonTrace> traceList = vo.getTraceList();
        for (BPersonTrace trace : traceList) {
            trace.setPersonId(personId);
            bPersonTraceDao.insert(trace);
        }

        bPersonFeeDao.deleteByPersonId(reportNo,personId);
        List<BPersonFee> feeList = vo.getFeeList();
        for (BPersonFee fee : feeList) {
            fee.setPersonId(personId);
            bPersonFeeDao.insert(fee);
        }
    }

    @Override
    public void submitPerson(PersonFormVo vo) {
        vo.getPersonMain().setUnderwriteFlag(UnderwriteFlagEnum.DOING.getCode());
        vo.getPersonMain().setPersonSubDate(new Date());
        savePerson(vo);
        generateWorkflow(vo.getPersonMain());
    }

    @Override
    public PersonFormVo initPersonApproveInfo(String reportNo, Integer businessKey) {
        BApprove approve = bApproveDao.selectById(businessKey);
        Integer personId = approve.getPersonId();
        PersonFormVo vo = new PersonFormVo();
        BPersonMain personMain = bPersonMainDao.selectById(personId);
        vo.setPersonMain(personMain);
        List<BPersonInjured> injuredList = bPersonInjuredDao.selectByPersonId(personId);
        vo.setInjuredList(injuredList);
//        List<BPersonTrace> traceList = bPersonTraceDao.selectByPersonId(personId);
//        vo.setTraceList(traceList);
        List<BPersonFee> feeList = bPersonFeeDao.selectByPersonId(personId);
        vo.setFeeList(feeList);
        return vo;
    }

    @Override
    public void personApprove(BApprove approve) {
        BPersonMain personMain = bPersonMainDao.selectById(approve.getPersonId());
        BClaimMain claimMain = bClaimMainDao.selectByReportNo(approve.getReportNo());
        //获取当前任务并设置完成
        BWorkflow preWorkflow = workflowDao.selectNodeByReportNo(personMain.getReportNo(),TaskTypeEnum.PersonApprove.getCode()+"_"+approve.getApproveNode(),approve.getApproveNo().toString());
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
            BigDecimal sumAmt = personMain.getSumAmt();
            //获取配置的审核流程
            SProcessMain processMain = sProcessMainDao.selectLastProcess(TaskTypeEnum.PersonApprove.getCode(), claimMain.getInsuranceCode());
            if (processMain == null) {
                //不存在针对险种的特殊配置则获取通用配置
                processMain = sProcessMainDao.selectCommonProcess(TaskTypeEnum.PersonApprove.getCode());
            }

            if (processMain != null) {
                SProcessNode node = sProcessNodeDao.selectByProcessIdAndNodeId(processMain.getId(), approve.getApproveNode());
                BigDecimal amount = node.getAmount();
                //不在权限范围内提交上一级
                if (amount != null && sumAmt.compareTo(amount) > 0) {
                    Integer nextNodeId = Integer.valueOf(approve.getApproveNode()) + 1;
                    SProcessNode nextNode = sProcessNodeDao.selectByProcessIdAndNodeId(processMain.getId(), nextNodeId.toString());

                    //存在下一节点
                    if (nextNode != null) {
                        BApprove bApprove = new BApprove();
                        bApprove.setPreApproveNo(approve.getApproveNo());
                        bApprove.setReportNo(claimMain.getReportNo());
                        bApprove.setClaimNo(claimMain.getClaimNo());
                        bApprove.setPersonId(personMain.getId());
                        bApprove.setApproveNode(nextNodeId.toString());
                        bApprove.setApproveTypeCode(TaskTypeEnum.PersonApprove.getCode());
                        bApprove.setApproveTypeName(TaskTypeEnum.PersonApprove.getName());
                        Integer approveNo = approveService.createApprove(bApprove);

                        //创建新任务
                        BWorkflow nextWorkflow = new BWorkflow();
                        nextWorkflow.setPrevTaskId(preWorkflow.getTaskId());
                        nextWorkflow.setTaskType(TaskTypeEnum.PersonApprove.getCode() + "_" + nextNodeId.toString());
                        nextWorkflow.setCreator(approve.getApproverCode());
                        nextWorkflow.setHandler(nextNode.getHandler());
                        nextWorkflow.setStartTime(new Date());
                        nextWorkflow.setTaskStatus(TaskStatusEnum.DOING.getCode());
                        nextWorkflow.setComCode(claimMain.getComCode());
                        nextWorkflow.setBusinessKey(approveNo.toString());
                        nextWorkflow.setReportNo(claimMain.getReportNo());
                        nextWorkflow.setPolicyNo(claimMain.getPolicyNo());
                        nextWorkflow.setInsurance(claimMain.getInsuranceCode());
                        nextWorkflow.setRemark(approve.getApproverCode() + " 提交人伤跟踪审核");
                        workflowDao.insert(nextWorkflow);
                        flag = false;
                    }
                }
            }

            //通过更新人伤跟踪信息
            if (flag) {
                personMain.setUnderwriteFlag(UnderwriteFlagEnum.DONE.getCode());
                personMain.setUnderwriteDate(new Date());
                bPersonMainDao.updateById(personMain);
            }
        } else {
            //退回
            personMain.setUnderwriteFlag(UnderwriteFlagEnum.INITORBACK.getCode());
            bPersonMainDao.updateById(personMain);
        }
    }

    @Override
    public List<Map> getDonePersonList(String reportNo, String queryString) {
        List<Map> list = bPersonMainDao.getDonePersonList(reportNo,queryString);
        return list;
    }

    @Override
    public Map getPersonInfo(Integer personId) {
        Map<String,Object> map = new HashMap<>();
        BPersonMain personMain = bPersonMainDao.selectById(personId);
        map.put("personMain",personMain);
        List<BPersonFee> feeList = bPersonFeeDao.selectByPersonId(personId);
        map.put("feeList",feeList);
        return map;
    }

    private void generateWorkflow(BPersonMain personMain) {
        String reportNo = personMain.getReportNo();
        SUser sUser = CommonUtil.getLoginUser();
        BWorkflow caseMain = workflowDao.selectCaseMain(reportNo);
        BClaimMain claimMain = bClaimMainDao.selectByReportNo(reportNo);
        //获取配置的审核流程
        String handler = processService.getHandler(TaskTypeEnum.PersonApprove,claimMain.getInsuranceCode());

        BWorkflow approve = new BWorkflow();
        approve.setPrevTaskId(caseMain.getTaskId());
        approve.setTaskType(TaskTypeEnum.PersonApprove.getCode()+"_1");
        approve.setCreator(sUser.getUserCode());
        if (StringUtils.isNotBlank(handler)) {
            //如果存在受理人则默认接收并给与开始时间
            approve.setHandler(handler);
            approve.setStartTime(new Date());
            approve.setTaskStatus(TaskStatusEnum.DOING.getCode());
        } else {
            approve.setTaskStatus(TaskStatusEnum.UNDO.getCode());
        }
        approve.setComCode(caseMain.getComCode());
        //创建审批节点数据
        BApprove bApprove = new BApprove();
        bApprove.setReportNo(reportNo);
        bApprove.setClaimNo(claimMain.getClaimNo());
        bApprove.setPersonId(personMain.getId());
        bApprove.setApproveNode("1");
        bApprove.setApproveTypeCode(TaskTypeEnum.PersonApprove.getCode());
        bApprove.setApproveTypeName(TaskTypeEnum.PersonApprove.getName());
        Integer approveNo = approveService.createApprove(bApprove);
        approve.setBusinessKey(approveNo.toString());
        approve.setReportNo(claimMain.getReportNo());
        approve.setPolicyNo(claimMain.getPolicyNo());
        approve.setInsurance(claimMain.getInsuranceCode());
        approve.setRemark(sUser.getUserName()+" 提交人伤跟踪审核");
        workflowDao.insert(approve);
    }
}
