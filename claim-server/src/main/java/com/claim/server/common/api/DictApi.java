package com.claim.server.common.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.claim.server.common.dao.SCommonDao;
import com.claim.server.common.dao.SCompanyDao;
import com.claim.server.common.dao.SUserDao;
import com.claim.server.common.po.SCommon;
import com.claim.server.common.po.SCompany;
import com.claim.server.common.po.SUser;
import com.claim.server.common.vo.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/dictionary")
public class DictApi {

    @Autowired
    private SCompanyDao sCompanyDao;

    @Autowired
    private SUserDao sUserDao;

    @Autowired
    private SCommonDao sCommonDao;

    @RequestMapping(value = "/companyDict",method = RequestMethod.POST)
    public IPage companyDict(@RequestBody JSONObject params) {
        String comName = params.getString("comName");
        String comLevel = params.getString("comLevel");
        String validStatus = params.getString("validStatus");
        QueryWrapper<SCompany> wrapper =  new QueryWrapper<>();
        if (StringUtils.isNotBlank(comName)) {
            wrapper.like("comName","%"+comName+"%").or().like("ClaimComName","%"+comName+"%");
        }
        if (StringUtils.isNotBlank(comLevel)) {
            wrapper.eq("comLevel",comLevel);
        }
        if (StringUtils.isNotBlank(validStatus)) {
            wrapper.eq("validStatus",validStatus);
        }
        Page<SCompany> page = new Page<>();
        page.setCurrent(params.getLong("page"));
        page.setSize(params.getLong("pageSize"));
        IPage<SCompany> result = sCompanyDao.selectPage(page,wrapper);
        return result;
    }

    @RequestMapping(value = "/userDict",method = RequestMethod.POST)
    public IPage userDict(@RequestBody JSONObject params) {
        String userCode = params.getString("userCode");
        String userName = params.getString("userName");
        String validStatus = params.getString("validStatus");
        QueryWrapper<SUser> wrapper =  new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)) {
            wrapper.like("userName","%"+userName+"%");
        }
        if (StringUtils.isNotBlank(userCode)) {
            wrapper.eq("userCode",userCode);
        }
        if (StringUtils.isNotBlank(validStatus)) {
            wrapper.eq("validStatus",validStatus);
        }
        Page<SUser> page = new Page<>();
        page.setCurrent(params.getLong("page"));
        page.setSize(params.getLong("pageSize"));
        IPage<SUser> result = sUserDao.selectPage(page,wrapper);
        return result;
    }

    @RequestMapping(value = "/companyLevel",method = RequestMethod.POST)
    public ApiResponse companyLevel(@RequestBody JSONObject params) {
        String comCode = params.getString("comCode");
        String comLevel = params.getString("comLevel");
        List<Map> list = sCompanyDao.initComSelect(comLevel,comCode);
        return new ApiResponse(list);
    }

    @RequestMapping(value = "editCompany",method = RequestMethod.POST)
    public ApiResponse editCompany(@RequestBody SCompany params) {
        sCompanyDao.updateById(params);
        return new ApiResponse();
    }

    @RequestMapping(value = "addCompany",method = RequestMethod.POST)
    public ApiResponse addCompany(@RequestBody SCompany params) {
        String comFullName = "";
        if (StringUtils.isNotBlank(params.getClaimComName())) {
            comFullName = params.getClaimComName() + "-" + params.getComName();
        } else {
            comFullName = params.getComName();
        }
        params.setComFullName(comFullName);
        sCompanyDao.insert(params);
        return new ApiResponse();
    }

    @RequestMapping(value = "checkComCode",method = RequestMethod.POST)
    public Boolean checkComCode(@RequestBody JSONObject params) {
        QueryWrapper<SCompany> wrapper =  new QueryWrapper<>();
        wrapper.eq("comCode",params.getString("comCode"));
        List list = sCompanyDao.selectList(wrapper);
        if (list.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @RequestMapping(value = "editUser",method = RequestMethod.POST)
    public ApiResponse editUser(@RequestBody SUser params) {
        if ("1".equals(params.getValidStatus())) {
            params.setValidStatus("0");
        } else {
            params.setValidStatus("1");
        }
        sUserDao.updateById(params);
        return new ApiResponse();
    }

    @RequestMapping(value = "/commonDict",method = RequestMethod.POST)
    public IPage commonDict(@RequestBody JSONObject params) {
        String dataTypeCode = params.getString("dataTypeCode");
        String dataName = params.getString("dataName");
        String validStatus = params.getString("validStatus");
        QueryWrapper<SCommon> wrapper =  new QueryWrapper<>();
        if (StringUtils.isNotBlank(dataName)) {
            wrapper.like("dataName","%"+dataName+"%");
        }
        if (StringUtils.isNotBlank(dataTypeCode)) {
            wrapper.eq("dataTypeCode",dataTypeCode);
        }
        if (StringUtils.isNotBlank(validStatus)) {
            wrapper.eq("validStatus",validStatus);
        }
        wrapper.orderByAsc("DataTypeCode","DataCode");
        Page<SCommon> page = new Page<>();
        page.setCurrent(params.getLong("page"));
        page.setSize(params.getLong("pageSize"));
        IPage<SCommon> result = sCommonDao.selectPage(page,wrapper);
        return result;
    }

    @RequestMapping(value = "/commonDataType",method = RequestMethod.POST)
    public ApiResponse commonDataType() {
        List<Map> list = sCommonDao.initDataType();
        return new ApiResponse(list);
    }

    @RequestMapping(value = "/selectByDataType",method = RequestMethod.POST)
    public ApiResponse selectByDataType(@RequestBody JSONObject params) {
        List<Map> list = sCommonDao.selectByDataType(params.getString("dataTypeCode"));
        return new ApiResponse(list);
    }

    @RequestMapping(value = "/getDamageCodes",method = RequestMethod.POST)
    public ApiResponse getDamageCodes() {
        List<Map> list = sCommonDao.getDamageCodes();
        return new ApiResponse(list);
    }

    @RequestMapping(value = "/getProvinceList",method = RequestMethod.POST)
    public ApiResponse getProvinceList() {
        List<Map> list = sCommonDao.getProvinceList();
        return new ApiResponse(list);
    }

    @RequestMapping(value = "/getCityList",method = RequestMethod.POST)
    public ApiResponse getCityList(@RequestBody JSONObject params) {
        List<Map> list = sCommonDao.getCityList(params.getString("preCode"));
        return new ApiResponse(list);
    }

    @RequestMapping(value = "/getCountyCodeList",method = RequestMethod.POST)
    public ApiResponse getCountyCodeList(@RequestBody JSONObject params) {
        List<Map> list = sCommonDao.getCountyCodeList(params.getString("preCode"));
        return new ApiResponse(list);
    }

    @RequestMapping(value = "/getCertifyList",method = RequestMethod.POST)
    public ApiResponse getCertifyList() {
        List<Map> list = sCommonDao.getCertifyList();
        return new ApiResponse(list);
    }

    @RequestMapping(value = "editCommon",method = RequestMethod.POST)
    public ApiResponse editCommon(@RequestBody SCommon params) {
        sCommonDao.updateById(params);
        return new ApiResponse();
    }

    @RequestMapping(value = "addCommon",method = RequestMethod.POST)
    public ApiResponse addCommon(@RequestBody SCommon params) {
        sCommonDao.insert(params);
        return new ApiResponse();
    }
}
