package com.claim.server.endcase.api;

import com.alibaba.fastjson.JSONObject;
import com.claim.server.common.vo.ApiResponse;
import com.claim.server.endcase.service.EndCaseService;
import com.claim.server.endcase.vo.EndCaseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/endcase")
public class EndCaseApi {

    @Autowired
    private EndCaseService endCaseService;

    @RequestMapping(value = "applyEndCase",method = RequestMethod.POST)
    public ApiResponse applyEndCase(@RequestBody JSONObject params) {
        String reportNo = params.getString("reportNo");
        Map map = endCaseService.applyEndCase(reportNo);
        return new ApiResponse(map);
    }

    @RequestMapping(value = "initEndCaseInfo",method = RequestMethod.POST)
    public ApiResponse initEndCaseInfo(@RequestBody JSONObject params) {
        String reportNo = params.getString("reportNo");
        String businessKey = params.getString("businessKey");
        EndCaseInfo endCaseInfo = endCaseService.initEndCaseInfo(reportNo,businessKey);
        return new ApiResponse(endCaseInfo);
    }
}
