package com.claim.server.person.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.claim.server.common.vo.ApiResponse;
import com.claim.server.person.dao.BPersonMainDao;
import com.claim.server.person.po.BPersonMain;
import com.claim.server.person.service.PersonService;
import com.claim.server.person.vo.PersonFormVo;
import com.claim.server.utils.UnderwriteFlagEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("person")
public class PersonApi {

    @Autowired
    private PersonService personService;

    @Autowired
    private BPersonMainDao bPersonMainDao;

    @RequestMapping(value = "/getPersonList",method = RequestMethod.POST)
    public IPage getPersonList(@RequestBody JSONObject params) {
        String reportNo = params.getString("reportNo");
        QueryWrapper<BPersonMain> wrapper =  new QueryWrapper<>();
        wrapper.eq("ReportNo",reportNo);
        wrapper.orderByAsc("CreateTime");
        Page<BPersonMain> page = new Page<>();
        page.setCurrent(params.getLong("page"));
        page.setSize(params.getLong("pageSize"));
        IPage<BPersonMain> result = bPersonMainDao.selectPage(page,wrapper);
        return result;
    }

    @RequestMapping(value = "initPersonInfo",method = RequestMethod.POST)
    public ApiResponse initPersonInfo(@RequestBody JSONObject params) {
        String reportNo = params.getString("reportNo");
        Integer personId = params.getInteger("personId");
        Map map = new HashMap();
        if (personId == null) {
            map = personService.createPersonInfo(reportNo);
        } else {
            map = personService.initPersonInfo(reportNo,personId);
        }
        return new ApiResponse(map);
    }

    @RequestMapping(value = "initPersonApproveInfo",method = RequestMethod.POST)
    public ApiResponse initPersonApproveInfo(@RequestBody JSONObject params) {
        String reportNo = params.getString("reportNo");
        Integer businessKey = params.getInteger("businessKey");
        PersonFormVo vo = personService.initPersonApproveInfo(reportNo,businessKey);
        return new ApiResponse(vo);
    }

    @RequestMapping(value = "getDonePersonList",method = RequestMethod.POST)
    public ApiResponse getDonePersonList(@RequestBody JSONObject params) {
        String reportNo = params.getString("reportNo");
        String queryString = params.getString("queryString");
        List<Map> list = personService.getDonePersonList(reportNo,queryString);
        return new ApiResponse(list);
    }

    @RequestMapping(value = "getPersonInfo",method = RequestMethod.POST)
    public ApiResponse getPersonInfo(@RequestBody JSONObject params) {
        Integer personId = params.getInteger("personId");
        Map map = personService.getPersonInfo(personId);
        return new ApiResponse(map);
    }

    @RequestMapping(value = "savePerson",method = RequestMethod.POST)
    public ApiResponse savePerson(@RequestBody PersonFormVo params) {
        params.getPersonMain().setUnderwriteFlag(UnderwriteFlagEnum.INITORBACK.getCode());
        personService.savePerson(params);
        return new ApiResponse(params);
    }

    @RequestMapping(value = "submitPerson",method = RequestMethod.POST)
    public ApiResponse submitPerson(@RequestBody PersonFormVo params) {
        params.getPersonMain().setUnderwriteFlag(UnderwriteFlagEnum.INITORBACK.getCode());
        personService.submitPerson(params);
        return new ApiResponse(params);
    }
}
