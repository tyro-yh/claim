package com.claim.server.grade.service.impl;

import com.claim.server.grade.dao.SGradeDao;
import com.claim.server.grade.dao.SUserGradeDao;
import com.claim.server.grade.po.SUserGrade;
import com.claim.server.grade.service.GradeService;
import com.claim.server.grade.vo.GradeFormVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private SGradeDao sGradeDao;

    @Autowired
    private SUserGradeDao sUserGradeDao;

    @Override
    public List<Map> getGradeList() {
        List<Map> list = sGradeDao.getGradeList();
        return list;
    }

    @Override
    public Map addUserGrade(GradeFormVo vo) {
        String userCode = vo.getUserCode();
        String userName = vo.getUserName();
        Map map = new HashMap();
        map.put("status","1");
        if (StringUtils.isBlank(userCode) || StringUtils.isBlank(userName)) {
            map.put("status","0");
            map.put("msg","员工号和员工名称不能为空");
            return map;
        }

        if (vo.getUserGradeList() == null || vo.getUserGradeList().isEmpty()) {
            map.put("status","0");
            map.put("msg","岗位不能为空");
            return map;
        }

        List<SUserGrade> list = sUserGradeDao.selectByUserCode(userCode);
        if ((list != null && !list.isEmpty())) {
            map.put("status","0");
            map.put("msg","员工已存在，不能重复新增");
            return map;
        }

        for (SUserGrade userGrade : vo.getUserGradeList()) {
            userGrade.setUserCode(userCode);
            userGrade.setUserName(userName);
            sUserGradeDao.insert(userGrade);
        }
        return map;
    }

    @Override
    public Map editUserGrade(GradeFormVo vo) {
        String userCode = vo.getUserCode();
        String userName = vo.getUserName();
        Map map = new HashMap();
        map.put("status","1");
        if (vo.getUserGradeList() == null || vo.getUserGradeList().isEmpty()) {
            map.put("status","0");
            map.put("msg","岗位不能为空");
            return map;
        }

        sUserGradeDao.deleteByUserCode(userCode);
        for (SUserGrade userGrade : vo.getUserGradeList()) {
            userGrade.setUserCode(userCode);
            userGrade.setUserName(userName);
            sUserGradeDao.insert(userGrade);
        }
        return map;
    }

    @Override
    public List<SUserGrade> initUserGrade(String userCode) {
        List<SUserGrade> list = sUserGradeDao.selectByUserCode(userCode);
        return list;
    }
}
