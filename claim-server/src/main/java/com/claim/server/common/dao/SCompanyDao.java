package com.claim.server.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.common.po.SCompany;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SCompanyDao extends BaseMapper<SCompany> {

    List<Map> initComSelect(@Param("comLevel") String comLevel,@Param("claimComCode") String claimComCode);

    SCompany selectByComCode(@Param("comCode") String comCode);
}
