package com.claim.server.interceptor;

import com.claim.server.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一日志管理
 */
@Component
public class LogInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("ThreadLocal StartTime");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long beginTime = System.currentTimeMillis(); //开始时间
        startTimeThreadLocal.set(beginTime); //线程绑定变量（该数据只有当前请求的线程可见）
        log.info("请求 {} 开始于: {}", request.getRequestURI(), CommonUtil.formatDateTime(beginTime,"yyyy-MM-dd HH:mm:ss.SSS"));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long beginTime = startTimeThreadLocal.get(); //得到线程绑定的局部变量（开始时间）
        long endTime = System.currentTimeMillis(); //结束时间
        log.info("请求 {} 结束于: {} 用时 {}ms", request.getRequestURI(), CommonUtil.formatDateTime(endTime,"yyyy-MM-dd HH:mm:ss.SSS"), endTime-beginTime);
    }
}
