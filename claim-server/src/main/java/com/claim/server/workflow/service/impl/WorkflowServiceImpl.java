package com.claim.server.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.claim.server.common.dao.SUserDao;
import com.claim.server.common.po.SUser;
import com.claim.server.utils.CommonUtil;
import com.claim.server.workflow.dao.BWorkflowDao;
import com.claim.server.workflow.po.BWorkflow;
import com.claim.server.workflow.service.WorkflowService;
import com.claim.server.workflow.vo.Edge;
import com.claim.server.workflow.vo.Meta;
import com.claim.server.workflow.vo.Node;
import com.claim.server.workflow.vo.NodeItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    private BWorkflowDao bWorkflowDao;

    @Autowired
    private SUserDao sUserDao;
    @Override
    public NodeItem buildNodeItem(String reportNo) {
        if (StringUtils.isBlank(reportNo)) {
            return null;
        }
        QueryWrapper<BWorkflow> wrapper = new QueryWrapper<>();
        wrapper.eq("ReportNo",reportNo);
        wrapper.isNull("PrevTaskId");
        //获取根节点即报案节点
        BWorkflow rootNode = bWorkflowDao.selectOne(wrapper);
        if (rootNode != null) {
            NodeItem nodeItem = new NodeItem();
            List<Node> nodes = new ArrayList<>();
            List<Edge> edges = new ArrayList<>();
            int x = 0;
            int y = 0;
            //递归获取子节点
            buildNodes(rootNode);
            buildEdges(rootNode,nodes,edges,x,y,null);
            nodeItem.setNodes(nodes);
            nodeItem.setEdges(edges);
            return nodeItem;
        } else {
            return null;
        }
    }

    private void buildNodes(BWorkflow po) {
        //获取上级节点是当前节点的任务
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq("PrevTaskId",po.getTaskId());
        List<BWorkflow> nextNodes = bWorkflowDao.selectList(wrapper);
        po.setNextNodes(nextNodes);
        //遍历递归获取在下级节点
        for (BWorkflow node : nextNodes) {
            buildNodes(node);
        }
    }

    private void buildEdges(BWorkflow po,List<Node> nodes,List<Edge> edges,int x,int y,Node preNode) {
        Node node = new Node();
        node.setId(String.valueOf(nodes.size()));
        node.setX(x);
        node.setY(y);
        Meta meta = new Meta();
        if (StringUtils.isNotBlank(po.getHandler())) {
            SUser sUser = sUserDao.selectByUserCode(po.getHandler());
            meta.setHandlerLabel(sUser.getUserName()+"("+po.getHandler()+")");
            meta.setHandler(sUser.getUserCode());
        } else {
            meta.setHandlerLabel("未接收");
            meta.setHandler("");
        }
        String taskType = CommonUtil.getTaskTypeName(po.getTaskType());
        String taskStatus = CommonUtil.getTaskStatusName(po.getTaskStatus());
        String taskTypeCode = CommonUtil.getTaskTypeCode(po.getTaskType());
        meta.setTaskType(taskTypeCode);
        Map params = new HashMap();
        params.put("businessKey",po.getBusinessKey());
        params.put("reportNo",po.getReportNo());
        params.put("taskStatus",po.getTaskStatus());
        meta.setParams(params);
        meta.setLink(taskType+"("+taskStatus+")");
        meta.setStartTime(CommonUtil.formatDateTime(po.getStartTime(),"yyyy-MM-dd HH:mm:ss"));
        meta.setEndTime(CommonUtil.formatDateTime(po.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
        node.setMeta(meta);
        node.setLabel(taskType);
        switch (po.getTaskStatus()) {
            case "0" : node.setColor("#ff5500");break;
            case "1" : node.setColor("#ffaa00");break;
            case "2" : node.setColor("#2196f3");break;
            case "-1" : node.setColor("#666666");break;
            default: node.setColor("#ff5500");
        }
        nodes.add(node);
        //如果存在上级节点则画边
        if (preNode != null) {
            Edge edge = new Edge();
            edge.setId("edge"+edges.size());
            edge.setSource(preNode.getId());
            edge.setTarget(node.getId());
            edges.add(edge);
        }
        y = y + 200;
        preNode = node;
        //递归画边
        for (BWorkflow nextNode : po.getNextNodes()) {
            buildEdges(nextNode,nodes,edges,x,y,preNode);
            x = x + 260;
        }
    }
}
