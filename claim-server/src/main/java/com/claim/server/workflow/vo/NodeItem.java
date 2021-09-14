package com.claim.server.workflow.vo;

import lombok.Data;

import java.util.List;

@Data
public class NodeItem {

    private List<Node> nodes;

    private List<Edge> edges;
}
