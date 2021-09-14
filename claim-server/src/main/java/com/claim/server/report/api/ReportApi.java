package com.claim.server.report.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.claim.server.common.dao.SCommonDao;
import com.claim.server.common.po.SUser;
import com.claim.server.common.vo.ApiResponse;
import com.claim.server.report.dao.BReportMainDao;
import com.claim.server.report.po.BReportMain;
import com.claim.server.utils.CaseFlagEnum;
import com.claim.server.utils.CommonUtil;
import com.claim.server.utils.TaskStatusEnum;
import com.claim.server.utils.TaskTypeEnum;
import com.claim.server.workflow.dao.BWorkflowDao;
import com.claim.server.workflow.po.BWorkflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/report")
public class ReportApi {

    @Autowired
    private BReportMainDao reportMainDao;

    @Autowired
    private BWorkflowDao workflowDao;

    @Autowired
    private SCommonDao sCommonDao;

    @RequestMapping(value = "/saveReport",method = RequestMethod.POST)
    public ApiResponse saveReport(@RequestBody BReportMain params) {
        params.setReportTime(new Date());
        params.setCaseFlag(CaseFlagEnum.Report.getCode());
        generateReportNo(params);
        reportMainDao.insert(params);
        generateWorkflow(params);
        return new ApiResponse();
    }

    @RequestMapping(value = "/getReport",method = RequestMethod.POST)
    public ApiResponse getReport(@RequestBody JSONObject params) {
        QueryWrapper<BReportMain> wrapper = new QueryWrapper<>();
        wrapper.eq("reportNo",params.getString("reportNo"));
        BReportMain bReportMain = reportMainDao.selectOne(wrapper);
        String province = sCommonDao.selectByDataTypeAndCode("province",bReportMain.getProvince());
        String city = sCommonDao.selectByDataTypeAndCode("city",bReportMain.getCity());
        String countyCode = sCommonDao.selectByDataTypeAndCode("countyCode",bReportMain.getCountyCode());
        bReportMain.setProvince(province);
        bReportMain.setCity(city);
        bReportMain.setCountyCode(countyCode);
        return new ApiResponse(bReportMain);
    }

    @RequestMapping(value = "/initReportInfo",method = RequestMethod.POST)
    public ApiResponse initReportInfo(@RequestBody JSONObject params) {
        String reportNo = params.getString("reportNo");
        BReportMain reportInfo = reportMainDao.selectByReportNo(reportNo);
        return new ApiResponse(reportInfo);
    }

    @RequestMapping(value = "/initCaseFlag",method = RequestMethod.POST)
    public ApiResponse initCaseFlag(@RequestBody JSONObject params) {
        String reportNo = params.getString("reportNo");
        String caseFlag = reportMainDao.selectCaseFlag(reportNo);
        return new ApiResponse(caseFlag);
    }

    private void generateWorkflow(BReportMain reportMain) {
        SUser sUser = CommonUtil.getLoginUser();
        BWorkflow workflow = new BWorkflow();
        workflow.setTaskType(TaskTypeEnum.Report.getCode());
        workflow.setTaskStatus(TaskStatusEnum.DONE.getCode());
        workflow.setCreator(sUser.getUserCode());
        workflow.setHandler(sUser.getUserCode());
        workflow.setStartTime(new Date());
        workflow.setEndTime(new Date());
        workflow.setComCode(reportMain.getComCode());
        workflow.setBusinessKey(reportMain.getReportNo());
        workflow.setReportNo(reportMain.getReportNo());
        workflow.setPolicyNo(reportMain.getPolicyNo());
        workflow.setInsurance(reportMain.getInsuranceCode());
        workflow.setRemark(sUser.getUserName()+TaskTypeEnum.Report.getName());
        workflowDao.insert(workflow);
        BWorkflow workflow2 = new BWorkflow();
        workflow2.setPrevTaskId(workflow.getTaskId());
        workflow2.setTaskType(TaskTypeEnum.CaseMain.getCode());
        workflow2.setTaskStatus(TaskStatusEnum.UNDO.getCode());
        workflow2.setCreator(sUser.getUserCode());
        workflow2.setComCode(workflow.getComCode());
        workflow2.setBusinessKey(workflow.getReportNo());
        workflow2.setReportNo(workflow.getReportNo());
        workflow2.setPolicyNo(workflow.getPolicyNo());
        workflow2.setInsurance(workflow.getInsurance());
        workflow2.setRemark("报案自动生成案件处理");
        workflowDao.insert(workflow2);
    }

    private void generateReportNo(BReportMain reportMain) {
        String reportTime = CommonUtil.formatDateTime(reportMain.getReportTime(),"yyyyMMddHHmmss");
        String insurance = reportMain.getInsuranceCode();
        String reportNo = "R"+insurance+reportTime;
        reportMain.setReportNo(reportNo);
    }
}
