package com.claim.server.grade.vo;

import com.claim.server.grade.po.SUserGrade;
import lombok.Data;

import java.util.List;

@Data
public class GradeFormVo {

    private String userName;

    private String userCode;

    private List<SUserGrade> userGradeList;
}
