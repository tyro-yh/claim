package com.claim.server.common.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.claim.server.common.dao.SBankDao;
import com.claim.server.common.dao.SLoginHisDao;
import com.claim.server.common.dao.STodoDao;
import com.claim.server.common.po.STodo;
import com.claim.server.common.vo.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/common")
public class CommonApi {

    @Autowired
    private STodoDao sTodoDao;

    @Autowired
    private SLoginHisDao sLoginHisDao;

    @Autowired
    private SBankDao sBankDao;

    @RequestMapping(value = "/selectTodoList")
    public ApiResponse selectTodoList(@RequestBody JSONObject params) {
        QueryWrapper<STodo> wrapper =  new QueryWrapper<>();
        wrapper.eq("userCode",params.getString("userCode"));
        wrapper.orderByDesc("createTime");
        List<STodo> list = sTodoDao.selectList(wrapper);
        return new ApiResponse(list);
    }

    @RequestMapping(value = "addTodo",method = RequestMethod.POST)
    public ApiResponse addTodo(@RequestBody STodo params) {
        sTodoDao.insert(params);
        return new ApiResponse();
    }

    @RequestMapping(value = "delTodo",method = RequestMethod.POST)
    public ApiResponse delTodo(@RequestBody JSONObject params) {
        sTodoDao.deleteById(params.getString("id"));
        return new ApiResponse();
    }

    @RequestMapping(value = "editTodo",method = RequestMethod.POST)
    public ApiResponse editTodo(@RequestBody STodo params, HttpServletRequest request) {
        sTodoDao.updateById(params);
        return new ApiResponse();
    }

    @RequestMapping(value = "selectLoginHis",method = RequestMethod.POST)
    public ApiResponse selectLoginHis(@RequestBody JSONObject params) {
        List<Map> list = sLoginHisDao.selectByUserCode(params.getString("userCode"));
        return new ApiResponse(list);
    }

    @RequestMapping(value = "checkAccountNo",method = RequestMethod.POST)
    public ApiResponse checkAccountNo(@RequestBody JSONObject params) {
        Map<String,Object> map = new HashMap<>();
        String accountNo = params.getString("accountNo");
        if (StringUtils.isNotBlank(accountNo)) {
            String kindCode = accountNo.substring(0,6);
            map = sBankDao.selectByKindCode(kindCode,String.valueOf(accountNo.length()));
            if (map != null) {
                String directBankCode = map.get("DirectBankCode").toString();
                List<Map> customs = sBankDao.selectByBankCode(directBankCode);
                map.put("customs",customs);
            }
        }
        return new ApiResponse(map);
    }
}
