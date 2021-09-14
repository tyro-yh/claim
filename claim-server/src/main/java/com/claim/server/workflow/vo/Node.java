package com.claim.server.workflow.vo;

import lombok.Data;

@Data
public class Node {

    private String id;

    private int x;

    private int y;

    private String label;

    private String color;

    private Meta meta;
}
