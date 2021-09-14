package com.claim.server.process.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.process.po.SProcessMain;
import org.apache.ibatis.annotations.Param;

public interface SProcessMainDao extends BaseMapper<SProcessMain> {

    SProcessMain selectLastProcess(@Param(value = "processType") String processType, @Param(value = "insurance") String insurance);

    SProcessMain selectCommonProcess(@Param(value = "processType") String processType);
}
