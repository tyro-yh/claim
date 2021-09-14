package com.claim.server.workflow.vo;

import lombok.Data;

import java.util.Map;

@Data
public class Meta {

    private String handler;

    private String handlerLabel;

    private String startTime;

    private String endTime;

    private String link;

    private String taskType;

    private Map params;
}
