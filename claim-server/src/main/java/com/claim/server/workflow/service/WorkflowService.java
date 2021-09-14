package com.claim.server.workflow.service;

import com.claim.server.workflow.vo.NodeItem;

public interface WorkflowService {

    NodeItem buildNodeItem(String reportNo);
}
