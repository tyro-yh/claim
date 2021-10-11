package com.claim.server.person.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.person.po.BPersonMain;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BPersonMainDao extends BaseMapper<BPersonMain> {

    List<Map> getDonePersonList(@Param(value = "reportNo") String reportNo,@Param(value = "queryString") String queryString);
}
