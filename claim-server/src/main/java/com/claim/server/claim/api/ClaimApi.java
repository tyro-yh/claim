package com.claim.server.claim.api;

import com.alibaba.fastjson.JSONObject;
import com.claim.server.claim.dao.BClaimMainDao;
import com.claim.server.claim.po.BClaimMain;
import com.claim.server.claim.service.ClaimService;
import com.claim.server.claim.vo.ClaimFormVo;
import com.claim.server.common.vo.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/claim")
public class ClaimApi {

    @Autowired
    private BClaimMainDao bClaimMainDao;

    @Autowired
    private ClaimService claimService;

    @RequestMapping(value = "/initClaimInfo",method = RequestMethod.POST)
    public ApiResponse initClaimInfo(@RequestBody JSONObject params) {
        String reportNo = params.getString("reportNo");
        Map map = new HashMap();
        BClaimMain claimMain = bClaimMainDao.selectByReportNo(reportNo);
        if (claimMain == null) {
            map = claimService.createClaimInfo(reportNo);
        } else {
            map.put("claimMain",claimMain);
            map = claimService.getClaimInfo(claimMain,map);
        }
        return new ApiResponse(map);
    }

    @RequestMapping(value = "saveClaim",method = RequestMethod.POST)
    public ApiResponse saveClaim(@RequestBody ClaimFormVo params) {
        params.getClaimMain().setClaimStatus("0");
        claimService.saveClaim(params);
        return new ApiResponse();
    }

    @RequestMapping(value = "submitClaim",method = RequestMethod.POST)
    public ApiResponse submitClaim(@RequestBody ClaimFormVo params) {
        params.getClaimMain().setClaimStatus("1");
        claimService.submitClaim(params);
        return new ApiResponse();
    }
}
