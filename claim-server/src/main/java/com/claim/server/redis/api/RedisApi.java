package com.claim.server.redis.api;

import com.alibaba.fastjson.JSONObject;
import com.claim.server.common.vo.ApiResponse;
import com.claim.server.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/redis")
public class RedisApi {

    @Autowired
    private RedisService redisService;

    @RequestMapping(value = "/remove",method = RequestMethod.POST)
    public ApiResponse remove(@RequestBody JSONObject params) {
        redisService.remove(params.getString("key"));
        return new ApiResponse();
    }
}
