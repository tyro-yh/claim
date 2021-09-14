package com.claim.server.process.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Node {
    private String id;
    private String text;
    private String handler;
    private BigDecimal amount;
}
