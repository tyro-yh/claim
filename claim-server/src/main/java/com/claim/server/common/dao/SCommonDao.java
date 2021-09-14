package com.claim.server.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.common.po.SCommon;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SCommonDao extends BaseMapper<SCommon> {

    List<Map> initDataType();

    List<Map> getDamageCodes();

    List<Map> getProvinceList();

    List<Map> getCityList(@Param(value = "preCode") String preCode);

    List<Map> getCountyCodeList(@Param(value = "preCode") String preCode);

    List<Map> selectByDataType(@Param(value = "dataTypeCode") String dataTypeCode);

    String selectByDataTypeAndCode(@Param(value = "dataTypeCode") String dataTypeCode,@Param(value = "dataCode") String dataCode);
}
