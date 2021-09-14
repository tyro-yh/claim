package com.claim.server.workflow.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.claim.server.common.vo.ApiResponse;
import com.claim.server.report.dao.BReportMainDao;
import com.claim.server.report.po.BReportMain;
import com.claim.server.workflow.dao.BWorkflowDao;
import com.claim.server.workflow.po.BWorkflow;
import com.claim.server.workflow.service.WorkflowService;
import com.claim.server.workflow.vo.NodeItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/workflow")
public class WorkflowApi {

    @Autowired
    private BWorkflowDao bWorkflowDao;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private BReportMainDao reportMainDao;

    @RequestMapping(value = "/myTask",method = RequestMethod.POST)
    public IPage myTask(@RequestBody JSONObject params) {
        QueryWrapper<BWorkflow> wrapper =  new QueryWrapper<>();
        wrapper.eq("handler",params.getString("userCode"));
        Page<BWorkflow> page = new Page<>();
        page.setCurrent(params.getLong("page"));
        page.setSize(params.getLong("pageSize"));
        IPage<BWorkflow> result = bWorkflowDao.selectPage(page,wrapper);
        return result;
    }

    @RequestMapping(value = "initTaskStatus",method = RequestMethod.POST)
    public ApiResponse initTaskStatus(@RequestBody JSONObject params) {
        String userCode = params.getString("userCode");
        List<Map> list = bWorkflowDao.initTaskStatus(userCode);
        return new ApiResponse(list);
    }

    @RequestMapping(value = "insuranceScale",method = RequestMethod.POST)
    public ApiResponse insuranceScale() {
        List<Map> list = bWorkflowDao.insuranceScale();
        return new ApiResponse(list);
    }

    @RequestMapping(value = "getWorkFlowList",method = RequestMethod.POST)
    public ApiResponse getWorkFlowList(@RequestBody JSONObject params) {
        BReportMain reportMain = reportMainDao.selectById(params.getString("reportNo"));
        NodeItem nodeItem = workflowService.buildNodeItem(params.getString("reportNo"));
        Map map = new HashMap();
        map.put("reportMain",reportMain);
        map.put("nodeItem",nodeItem);
        return new ApiResponse(map);
    }
}
