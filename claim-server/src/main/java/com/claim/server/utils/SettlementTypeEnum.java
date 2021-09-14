package com.claim.server.utils;

public enum SettlementTypeEnum {
    P("P","赔款理算"),
    F("F","费用理算"),
    Y("Y","预赔理算");

    private String code;

    private String name;

    SettlementTypeEnum(String code,String name) {
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
