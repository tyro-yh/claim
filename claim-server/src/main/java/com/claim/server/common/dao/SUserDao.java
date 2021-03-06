package com.claim.server.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.common.po.SUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SUserDao extends BaseMapper<SUser> {

    SUser loginCheck(SUser sUser);

    SUser selectByUserCode(@Param(value = "userCode") String userCode);

    List<Map> getHandlerList(@Param(value = "queryString") String queryString);
}
