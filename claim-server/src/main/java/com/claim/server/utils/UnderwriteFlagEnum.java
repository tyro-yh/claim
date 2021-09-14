package com.claim.server.utils;

public enum UnderwriteFlagEnum {

    INITORBACK("0","初始化/退回"),
    DOING("1","审核中"),
    DONE("2","审核完毕"),
    CANCEL("-1","已注销");


    private String code;

    private String name;

    UnderwriteFlagEnum(String code,String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
