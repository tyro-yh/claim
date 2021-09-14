package com.claim.server.utils;

import com.claim.server.common.vo.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler
    public ApiResponse runtimeExceptHandler(Exception e) {
        log.error(CommonUtil.getStackTraceAsString(e));
        return new ApiResponse(e);
    }
}
