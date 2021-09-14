package com.claim.server.common.vo;

import lombok.Data;

import java.util.Date;

@Data
public class LoginUser {

    private String userCode;

    private String userName;

    private String comCode;

    private String comFullName;

    private Date lastLoginTime;

    private String token;
}
