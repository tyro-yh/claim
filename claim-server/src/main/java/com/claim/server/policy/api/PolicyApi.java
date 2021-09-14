package com.claim.server.policy.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.claim.server.common.vo.ApiResponse;
import com.claim.server.policy.dao.BPolicyMainDao;
import com.claim.server.policy.po.BPolicyMain;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/policy")
public class PolicyApi {

    @Autowired
    private BPolicyMainDao bPolicyMainDao;

    @RequestMapping(value = "/getPolicyList",method = RequestMethod.POST)
    public IPage getPolicyList(@RequestBody JSONObject params) {
        String policyNo = params.getString("policyNo");
        String insuredCode = params.getString("insuredCode");
        String appliCode = params.getString("appliCode");
        QueryWrapper<BPolicyMain> wrapper =  new QueryWrapper<>();
        if (StringUtils.isNotBlank(policyNo)) {
            wrapper.eq("policyNo",policyNo);
        }
        if (StringUtils.isNotBlank(insuredCode)) {
            wrapper.eq("insuredCode",insuredCode);
        }
        if (StringUtils.isNotBlank(appliCode)) {
            wrapper.eq("appliCode",appliCode);
        }
        Page<BPolicyMain> page = new Page<>();
        page.setCurrent(params.getLong("page"));
        page.setSize(params.getLong("pageSize"));
        IPage<BPolicyMain> result = bPolicyMainDao.selectPage(page,wrapper);
        return result;
    }

    @RequestMapping(value = "/initPolicyInfo",method = RequestMethod.POST)
    public ApiResponse initPolicyInfo(@RequestBody JSONObject params) {
        String reportNo = params.getString("reportNo");
        BPolicyMain policyInfo = bPolicyMainDao.selectByReportNo(reportNo);
        return new ApiResponse(policyInfo);
    }
}
