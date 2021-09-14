package com.claim.server.settlement.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.claim.server.common.vo.ApiResponse;
import com.claim.server.settlement.dao.BSettlementMainDao;
import com.claim.server.settlement.po.BSettlementMain;
import com.claim.server.settlement.service.SettlementService;
import com.claim.server.settlement.vo.SettlementFormVo;
import com.claim.server.utils.UnderwriteFlagEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "settlement")
public class SettlementApi {

    @Autowired
    private BSettlementMainDao bSettlementMainDao;

    @Autowired
    private SettlementService settlementService;

    @RequestMapping(value = "/getSettlementList",method = RequestMethod.POST)
    public IPage getSettlementList(@RequestBody JSONObject params) {
        String reportNo = params.getString("reportNo");
        QueryWrapper<BSettlementMain> wrapper =  new QueryWrapper<>();
        wrapper.eq("ReportNo",reportNo);
        wrapper.orderByAsc("CreateTime");
        Page<BSettlementMain> page = new Page<>();
        page.setCurrent(params.getLong("page"));
        page.setSize(params.getLong("pageSize"));
        IPage<BSettlementMain> result = bSettlementMainDao.selectPage(page,wrapper);
        return result;
    }

    @RequestMapping(value = "/initSettlementInfo",method = RequestMethod.POST)
    public ApiResponse initSettlementInfo(@RequestBody JSONObject params) {
        String reportNo = params.getString("reportNo");
        String settlementType = params.getString("settlementType");
        String settlementNo = params.getString("settlementNo");
        Map map = new HashMap();
        if (StringUtils.isBlank(settlementNo)) {
            map = settlementService.createSettlementInfo(reportNo,settlementType);
        } else {
            map = settlementService.initSettlementInfo(reportNo,settlementNo);
        }
        return new ApiResponse(map);
    }

    @RequestMapping(value = "/initSettlementContent",method = RequestMethod.POST)
    public ApiResponse initSettlementContent(@RequestBody SettlementFormVo params) {
        settlementService.buildContent(params);
        return new ApiResponse(params);
    }

    @RequestMapping(value = "saveSettlement",method = RequestMethod.POST)
    public ApiResponse saveSettlement(@RequestBody SettlementFormVo params) {
        params.getSettlementMain().setUnderwriteFlag(UnderwriteFlagEnum.INITORBACK.getCode());
        settlementService.saveSettlement(params);
        return new ApiResponse();
    }

    @RequestMapping(value = "submitSettlement",method = RequestMethod.POST)
    public ApiResponse submitSettlement(@RequestBody SettlementFormVo params) {
        params.getSettlementMain().setUnderwriteFlag(UnderwriteFlagEnum.INITORBACK.getCode());
        settlementService.submitSettlement(params);
        return new ApiResponse();
    }

    @RequestMapping(value = "cancelSettlement",method = RequestMethod.POST)
    public ApiResponse cancelSettlement(@RequestBody JSONObject params) {
        String settlementNo = params.getString("settlementNo");
        settlementService.cancelSettlement(settlementNo);
        return new ApiResponse();
    }

    @RequestMapping(value = "initSettlementForUndwrt",method = RequestMethod.POST)
    public ApiResponse initSettlementForUndwrt(@RequestBody JSONObject params) {
        String reportNo = params.getString("reportNo");
        String businessKey = params.getString("businessKey");
        Map map = settlementService.initSettlementForUndwrt(reportNo,businessKey);
        return new ApiResponse(map);
    }

    @RequestMapping(value = "checkSettlementForAdd",method = RequestMethod.POST)
    public ApiResponse checkSettlementForAdd(@RequestBody JSONObject params) {
        String reportNo = params.getString("reportNo");
        String settlementType = params.getString("settlementType");
        Map map = settlementService.checkSettlementForAdd(reportNo,settlementType);
        return new ApiResponse(map);
    }

    @RequestMapping(value = "checkSettlementForSubmit",method = RequestMethod.POST)
    public ApiResponse checkSettlementForSubmit(@RequestBody SettlementFormVo params) {
        Map map = settlementService.checkSettlementForSubmit(params);
        return new ApiResponse(map);
    }
}
