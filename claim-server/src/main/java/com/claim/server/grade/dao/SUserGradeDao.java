package com.claim.server.grade.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.grade.po.SUserGrade;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SUserGradeDao extends BaseMapper<SUserGrade> {

    void deleteByUserCode(@Param(value = "userCode") String userCode);

    List<SUserGrade> selectByUserCode(@Param(value = "userCode") String userCode);
}
