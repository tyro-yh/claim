package com.claim.server.person.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.person.po.BPersonTrace;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BPersonTraceDao extends BaseMapper<BPersonTrace> {

    void deleteByPersonId(@Param(value = "reportNo") String reportNo, @Param(value = "personId") Integer personId);

    List<BPersonTrace> selectByPersonId(@Param(value = "personId") Integer personId);
}
