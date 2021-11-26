package com.claim.server.grade.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.claim.server.common.vo.ApiResponse;
import com.claim.server.grade.dao.SUserGradeDao;
import com.claim.server.grade.po.SUserGrade;
import com.claim.server.grade.service.GradeService;
import com.claim.server.grade.vo.GradeFormVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/grade")
public class GradeApi {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private SUserGradeDao sUserGradeDao;

    @RequestMapping(value = "getGradeList",method = RequestMethod.POST)
    public ApiResponse getGradeList() {
        List<Map> list = gradeService.getGradeList();
        return new ApiResponse(list);
    }

    @RequestMapping(value = "/getUserGradeList",method = RequestMethod.POST)
    public IPage getUserGradeList(@RequestBody JSONObject params) {
        String name = params.getString("name");
        String code = params.getString("code");
        QueryWrapper<SUserGrade> wrapper =  new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            wrapper.like("UserName",name);
        }
        if (StringUtils.isNotBlank(code)) {
            wrapper.like("UserCode",code);
        }
        wrapper.groupBy("UserCode");
        wrapper.orderByAsc("CreateTime");
        Page<SUserGrade> page = new Page<>();
        page.setCurrent(params.getLong("page"));
        page.setSize(params.getLong("pageSize"));
        IPage<SUserGrade> result = sUserGradeDao.selectPage(page,wrapper);
        return result;
    }

    @RequestMapping(value = "addUserGrade",method = RequestMethod.POST)
    public ApiResponse addUserGrade(@RequestBody GradeFormVo params) {
        Map map = gradeService.addUserGrade(params);
        return new ApiResponse(map);
    }

    @RequestMapping(value = "delUserGrade",method = RequestMethod.POST)
    public void delUserGrade(@RequestBody JSONObject params) {
        String userCode = params.getString("userCode");
        sUserGradeDao.deleteByUserCode(userCode);
    }

    @RequestMapping(value = "initUserGrade",method = RequestMethod.POST)
    public ApiResponse initUserGrade(@RequestBody JSONObject params) {
        String userCode = params.getString("userCode");
        List<SUserGrade> list = gradeService.initUserGrade(userCode);
        return new ApiResponse(list);
    }

    @RequestMapping(value = "editUserGrade",method = RequestMethod.POST)
    public ApiResponse editUserGrade(@RequestBody GradeFormVo params) {
        Map map = gradeService.editUserGrade(params);
        return new ApiResponse(map);
    }
}
