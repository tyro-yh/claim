package com.claim.server.person.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.person.po.BPersonFee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BPersonFeeDao extends BaseMapper<BPersonFee> {

    void deleteByPersonId(@Param(value = "reportNo") String reportNo, @Param(value = "personId") Integer personId);

    List<BPersonFee> selectByPersonId(@Param(value = "personId") Integer personId);
}
