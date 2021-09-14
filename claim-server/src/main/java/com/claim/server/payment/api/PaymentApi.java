package com.claim.server.payment.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.claim.server.common.vo.ApiResponse;
import com.claim.server.payee.dao.BPayeeDao;
import com.claim.server.payee.po.BPayee;
import com.claim.server.payment.dao.BPaymentDao;
import com.claim.server.payment.po.BPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("payment")
public class PaymentApi {

    @Autowired
    private BPaymentDao bPaymentDao;

    @Autowired
    private BPayeeDao bPayeeDao;

    @RequestMapping(value = "initPaymentList",method = RequestMethod.POST)
    public ApiResponse initPaymentList(@RequestBody JSONObject params) {
        String settlementNo = params.getString("settlementNo");
        List<BPayment> paymentList = bPaymentDao.selectBySettlementNo(settlementNo);
        for (BPayment payment : paymentList) {
            Integer payeeId = payment.getPayeeId();
            BPayee payee = bPayeeDao.selectById(payeeId);
            payment.setPayeeInfo(payee);
        }
        return new ApiResponse(paymentList);
    }

    /*@RequestMapping(value = "savePayment",method = RequestMethod.POST)
    public ApiResponse savePayment(@RequestBody List<BPayment> params) {
        if (params != null && params.size() > 0) {
            String reportNo = params.get(0).getReportNo();
            String settlementNo = params.get(0).getSettlementNo();
            bPaymentDao.deleteBySettlementNo(reportNo,settlementNo);
            for (BPayment payment : params) {
                bPaymentDao.insert(payment);
            }
        }
        return new ApiResponse();
    }*/
}
