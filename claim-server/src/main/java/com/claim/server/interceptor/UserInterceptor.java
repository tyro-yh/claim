package com.claim.server.interceptor;

import com.claim.server.common.po.SUser;
import com.claim.server.redis.service.RedisService;
import com.claim.server.utils.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截
 */
@Component
public class UserInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        //判断token是否过期
        Boolean flag = redisService.exists(token);
        if (!flag) {
            response.setHeader("logout","1");
        } else {
            //获取token代表的用户信息
            SUser user = (SUser) redisService.get(token);
            //放进request域便于流程取用
            request.setAttribute("loginUser",user);
            //刷新过期时间
            redisService.reExpire(token);
        }
        return flag;
    }
}
