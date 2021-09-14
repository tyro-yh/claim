package com.claim.server.payee.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.payee.po.BPayee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BPayeeDao extends BaseMapper<BPayee> {

    BPayee selectUniquePayee(@Param(value = "reportNo") String reportNo, @Param(value = "certifyNo") String certifyNo, @Param(value = "accountNo") String accountNo);
}