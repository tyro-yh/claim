package com.claim.server.grade.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.grade.po.SGrade;

import java.util.List;
import java.util.Map;

public interface SGradeDao extends BaseMapper<SGrade> {

    List<Map> getGradeList();
}
