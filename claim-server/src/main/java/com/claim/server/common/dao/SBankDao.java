package com.claim.server.common.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SBankDao {

    Map selectByKindCode(@Param(value = "kindCode") String kindCode,@Param(value = "cardLength") String cardLength);

    List<Map> selectByBankCode(@Param(value = "bankCode") String bankCode);
}
