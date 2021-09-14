package com.claim.server.utils;

public enum CaseFlagEnum {
    Report("01","已报案"),
    Claim("02","已立案"),
    Settlement("03","已理算"),
    EndCase("04","已结案"),
    ReOpen("05","已重开");

    private String code;

    private String name;

    CaseFlagEnum(String code,String name) {
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
