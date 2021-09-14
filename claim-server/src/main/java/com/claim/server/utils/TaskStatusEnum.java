package com.claim.server.utils;

public enum TaskStatusEnum {
    UNDO("0","未处理"),
    DOING("1","处理中"),
    DONE("2","已处理"),
    BACK("-1","已退回");


    private String code;

    private String name;

    TaskStatusEnum(String code,String name) {
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
