package com.claim.server.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.common.po.SLoginHis;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SLoginHisDao extends BaseMapper<SLoginHis> {

    void deleteHis(@Param(value = "userCode") String userCode);

    Date lastLoginTime(@Param(value = "userCode") String userCode);

    List<Map> selectByUserCode(@Param(value = "userCode") String userCode);
}
