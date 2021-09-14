package com.claim.server.common.api;

import com.alibaba.fastjson.JSONObject;
import com.claim.server.common.dao.SCompanyDao;
import com.claim.server.common.dao.SLoginHisDao;
import com.claim.server.common.dao.SUserDao;
import com.claim.server.common.po.SCompany;
import com.claim.server.common.po.SLoginHis;
import com.claim.server.common.po.SUser;
import com.claim.server.common.vo.ApiResponse;
import com.claim.server.common.vo.LoginUser;
import com.claim.server.common.vo.ResponseCode;
import com.claim.server.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RestController
public class LoginApi {

    @Autowired
    private SUserDao sUserDao;

    @Autowired
    private SCompanyDao sCompanyDao;

    @Autowired
    private SLoginHisDao sLoginHisDao;

    @Autowired
    private RedisService redisService;

    @CrossOrigin("*")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ApiResponse login(@RequestBody JSONObject params) {
        String userCode = params.getString("userCode");
        SUser sUser = new SUser();
        sUser.setUserCode(userCode);
        sUser.setPassWord(params.getString("passWord"));
        SUser loginUser = sUserDao.loginCheck(sUser);
        if (loginUser != null) {
            //生成token并放入redis
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime ldt = LocalDateTime.now();
            String slat = userCode+dtf.format(ldt);
            String token = DigestUtils.md5DigestAsHex(slat.getBytes());
            redisService.set(token,loginUser);

            //组装登录用户信息
            Date lastLoginTime = sLoginHisDao.lastLoginTime(userCode);
            if (lastLoginTime == null) {
                lastLoginTime = new Date();
            }
            SCompany sCompany = sCompanyDao.selectByComCode(loginUser.getComCode());
            LoginUser luser = new LoginUser();
            luser.setUserCode(loginUser.getUserCode());
            luser.setUserName(loginUser.getUserName());
            luser.setComCode(loginUser.getComCode());
            luser.setComFullName(sCompany.getComFullName());
            luser.setLastLoginTime(lastLoginTime);
            luser.setToken(token);

            //保留登录历史
            this.loginHis(userCode);
            return new ApiResponse(luser);
        } else {
            return new ApiResponse(ResponseCode.FAIL,"账号密码错误",null);
        }
    }

    /**
     * 保留登录历史
     * @param userCode
     */
    private void loginHis(String userCode) {
        SLoginHis sLoginHis = new SLoginHis();
        sLoginHis.setUserCode(userCode);
        sLoginHis.setLoginTime(new Date());
        sLoginHisDao.insert(sLoginHis);
        sLoginHisDao.deleteHis(userCode);
    }
}
