package com.claim.server.grade.service;

import com.claim.server.grade.po.SUserGrade;
import com.claim.server.grade.vo.GradeFormVo;

import java.util.List;
import java.util.Map;

public interface GradeService {

    List<Map> getGradeList();

    Map addUserGrade(GradeFormVo vo);

    Map editUserGrade(GradeFormVo vo);

    List<SUserGrade> initUserGrade(String userCode);
}
