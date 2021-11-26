package com.claim.server.menu.dao;

import com.claim.server.menu.po.SMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SMenuDao {

    List<SMenu> selectUserMenu(@Param(value = "userCode") String userCode);

    List<SMenu> selectByParentId(@Param(value = "userCode") String userCode,@Param(value = "parentId") Integer parentId);
}
