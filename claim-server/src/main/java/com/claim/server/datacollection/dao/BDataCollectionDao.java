package com.claim.server.datacollection.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.datacollection.po.BDataCollection;
import org.apache.ibatis.annotations.Param;

public interface BDataCollectionDao extends BaseMapper<BDataCollection> {

    BDataCollection selectByReportNoAndCertifyCode(@Param(value = "reportNo") String reportNo,@Param(value = "certifyCode") String certifyCode);
}
