package com.claim.server.payee.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.claim.server.common.vo.ApiResponse;
import com.claim.server.common.vo.ResponseCode;
import com.claim.server.payee.dao.BPayeeDao;
import com.claim.server.payee.po.BPayee;
import com.claim.server.payee.service.PayeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("payee")
public class PayeeApi {

    @Autowired
    private BPayeeDao bPayeeDao;

    @RequestMapping(value = "initPayeeList",method = RequestMethod.POST)
    public IPage initPayeeList(@RequestBody JSONObject params) {
        String reportNo = params.getString("reportNo");
        QueryWrapper<BPayee> wrapper =  new QueryWrapper<>();
        wrapper.eq("ReportNo",reportNo);
        Page<BPayee> page = new Page<>();
        page.setCurrent(params.getLong("page"));
        page.setSize(params.getLong("pageSize"));
        IPage<BPayee> result = bPayeeDao.selectPage(page,wrapper);
        return result;
    }

    @RequestMapping(value = "savePayee",method = RequestMethod.POST)
    public ApiResponse savePayee(@RequestBody BPayee params) {
        String reportNo = params.getReportNo();
        String certifyNo = params.getCertifyNo();
        String accountNo = params.getAccountNo();
        BPayee payee = bPayeeDao.selectUniquePayee(reportNo,certifyNo,accountNo);
        if (params.getId() != null) {
            if (payee != null && payee.getId() != params.getId()) {
                return new ApiResponse(ResponseCode.INTERNAL_SERVER_ERROR,"该收款人已存在");
            } else {
                bPayeeDao.updateById(params);
            }
        } else {
            if (payee != null) {
                return new ApiResponse(ResponseCode.INTERNAL_SERVER_ERROR,"该收款人已存在");
            } else {
                if (!"1".equals(params.getAccountType())) {
                    params.setBankName(null);
                    params.setBankCode(null);
                    params.setBankOutlets(null);
                    params.setBankNo(null);
                }
                bPayeeDao.insert(params);
            }
        }
        return new ApiResponse();
    }
}