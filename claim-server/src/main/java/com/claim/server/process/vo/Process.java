package com.claim.server.process.vo;

import lombok.Data;

import java.util.Date;

@Data
public class Process {
    private Integer id;
    private String processType;
    private String title;
    private String[] insurance;
    private Date createTime;
    private Date updateTime;
}
