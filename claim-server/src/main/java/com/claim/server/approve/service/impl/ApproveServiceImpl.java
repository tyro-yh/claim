package com.claim.server.approve.service.impl;

import com.claim.server.approve.dao.BApproveDao;
import com.claim.server.approve.po.BApprove;
import com.claim.server.approve.service.ApproveService;
import com.claim.server.claim.service.ClaimService;
import com.claim.server.common.po.SUser;
import com.claim.server.common.vo.LoginUser;
import com.claim.server.endcase.service.EndCaseService;
import com.claim.server.settlement.service.SettlementService;
import com.claim.server.utils.CommonUtil;
import com.claim.server.utils.TaskTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApproveServiceImpl implements ApproveService {

    @Autowired
    private BApproveDao bApproveDao;

    @Autowired
    private ClaimService claimService;

    @Autowired
    private SettlementService settlementService;

    @Autowired
    private EndCaseService endCaseService;

    @Override
    public Integer createApprove(BApprove bApprove) {
        bApproveDao.insert(bApprove);
        return bApprove.getApproveNo();
    }

    @Override
    public BApprove initApproveInfo(Integer approveNo, List<String> preContent) {
        BApprove bApprove = bApproveDao.selectById(Integer.valueOf(approveNo));
        if (bApprove.getPreApproveNo() != null) {
            BApprove preApprove = initApproveInfo(bApprove.getPreApproveNo(),preContent);
            preContent.add(CommonUtil.formatDateTime(preApprove.getApproveDate())+" "+preApprove.getApproverName()+"："+preApprove.getContent());
        }
        bApprove.setPreContent(preContent);
        return bApprove;
    }

    @Override
    public Map saveApprove(BApprove bApprove) {
        Map map = new HashMap();
        map.put("status","1");
        BApprove po = bApproveDao.selectById(bApprove.getApproveNo());
        if (po.getApproveDate() != null) {
            map.put("status","0");
            map.put("msg","该流程已提交，不能重复提交");
        }
        bApproveDao.updateById(bApprove);
        return map;
    }

    @Override
    public Map submitApprove(BApprove bApprove) {
        Map map = new HashMap();
        map.put("status","1");
        BApprove po = bApproveDao.selectById(bApprove.getApproveNo());
        if (po.getApproveDate() != null) {
            map.put("status","0");
            map.put("msg","该流程已提交，不能重复提交");
        }
        SUser user = CommonUtil.getLoginUser();
        bApprove.setApproveDate(new Date());
        bApprove.setApproverCode(user.getUserCode());
        bApprove.setApproverName(user.getUserName());
        saveApprove(bApprove);
        if (TaskTypeEnum.ClaimApprove.getCode().equals(bApprove.getApproveTypeCode())) {
            claimService.claimApprove(bApprove);
        } else if (TaskTypeEnum.Undwrt.getCode().equals(bApprove.getApproveTypeCode())) {
            settlementService.undwrt(bApprove);
        } else if (TaskTypeEnum.EndCase.getCode().equals(bApprove.getApproveTypeCode())) {
            endCaseService.endCase(bApprove);
        }
        return map;
    }

    @Override
    public List<BApprove> getApproveForSettlement(String settlementNo) {
        return bApproveDao.selectBySettlementNo(settlementNo);
    }

    @Override
    public List<BApprove> selectEndCaseDoing(String reportNo) {
        return bApproveDao.selectEndCaseDoing(reportNo);
    }
}
