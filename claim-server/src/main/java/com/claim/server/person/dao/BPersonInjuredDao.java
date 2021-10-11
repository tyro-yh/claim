package com.claim.server.person.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.person.po.BPersonInjured;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BPersonInjuredDao extends BaseMapper<BPersonInjured> {

    void deleteByPersonId(@Param(value = "reportNo") String reportNo, @Param(value = "personId") Integer personId);

    List<BPersonInjured> selectByPersonId(@Param(value = "personId") Integer personId);
}
