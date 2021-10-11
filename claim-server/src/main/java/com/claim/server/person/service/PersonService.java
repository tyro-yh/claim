package com.claim.server.person.service;

import com.claim.server.approve.po.BApprove;
import com.claim.server.person.vo.PersonFormVo;

import java.util.List;
import java.util.Map;

public interface PersonService {

    Map createPersonInfo(String reportNo);

    Map initPersonInfo(String reportNo,Integer personId);

    void savePerson(PersonFormVo vo);

    void submitPerson(PersonFormVo vo);

    PersonFormVo initPersonApproveInfo(String reportNo,Integer businessKey);

    void personApprove(BApprove approve);

    List<Map> getDonePersonList(String reportNo,String queryString);

    Map getPersonInfo(Integer personId);
}
