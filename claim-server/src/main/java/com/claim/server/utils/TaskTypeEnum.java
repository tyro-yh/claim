package com.claim.server.utils;

public enum TaskTypeEnum {
    Report("Report","报案"),
    CaseMain("CaseMain","案件受理"),
    ClaimApprove("ClaimApprove","立案审核"),
    Settlement("Settlement","理算"),
    Undwrt("Undwrt","核赔"),
    EndCase("EndCase","结案审核");

    private String code;

    private String name;

    TaskTypeEnum(String code,String name) {
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
