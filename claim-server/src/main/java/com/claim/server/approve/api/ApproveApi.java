package com.claim.server.approve.api;

import com.alibaba.fastjson.JSONObject;
import com.claim.server.approve.po.BApprove;
import com.claim.server.approve.service.ApproveService;
import com.claim.server.common.vo.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/approve")
public class ApproveApi {

    @Autowired
    private ApproveService approveService;

    @RequestMapping(value = "/initApproveInfo",method = RequestMethod.POST)
    public ApiResponse initApproveInfo(@RequestBody JSONObject params) {
        String approveNo = params.getString("approveNo");
        BApprove bApprove = approveService.initApproveInfo(Integer.valueOf(approveNo),new ArrayList<>());
        return new ApiResponse(bApprove);
    }

    @RequestMapping(value = "/saveApprove",method = RequestMethod.POST)
    public ApiResponse saveApprove(@RequestBody BApprove params) {
        Map map = approveService.saveApprove(params);
        return new ApiResponse(map);
    }

    @RequestMapping(value = "/submitApprove",method = RequestMethod.POST)
    public ApiResponse submitApprove(@RequestBody BApprove params) {
        Map map = approveService.submitApprove(params);
        return new ApiResponse(map);
    }

    @RequestMapping(value = "/getApproveForSettlement",method = RequestMethod.POST)
    public ApiResponse getApproveForSettlement(@RequestBody JSONObject params) {
        String settlementNo = params.getString("settlementNo");
        List<BApprove> list = approveService.getApproveForSettlement(settlementNo);
        return new ApiResponse(list);
    }
}
