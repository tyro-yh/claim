package com.claim.server.utils;

import com.claim.server.interceptor.LogInterceptor;
import com.claim.server.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private LogInterceptor logInterceptor;

    @Autowired
    private UserInterceptor userInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //用户token拦截
        InterceptorRegistration irUser = registry.addInterceptor(userInterceptor);
        irUser.order(0);
        irUser.addPathPatterns("/**");                      //所有路径都被拦截
        irUser.excludePathPatterns(                         //添加不拦截路径
                "/login",            //登录
                "/redis/*"
        );

        //日志拦截
        InterceptorRegistration irLog = registry.addInterceptor(logInterceptor);
        irLog.order(1);
        irLog.addPathPatterns("/**");                      //所有路径都被拦截
        irLog.excludePathPatterns(                         //添加不拦截路径
                "/login"            //登录
        );


    }
}
