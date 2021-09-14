package com.claim.server.process.vo;

import lombok.Data;

import java.util.List;

@Data
public class SProcessVo {

    private Process process;

    private List<Node> nodes;
}
