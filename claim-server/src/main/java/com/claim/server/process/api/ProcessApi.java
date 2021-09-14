package com.claim.server.process.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.claim.server.common.vo.ApiResponse;
import com.claim.server.process.dao.SProcessMainDao;
import com.claim.server.process.dao.SProcessNodeDao;
import com.claim.server.process.po.SProcessMain;
import com.claim.server.process.po.SProcessNode;
import com.claim.server.process.vo.Node;
import com.claim.server.process.vo.Process;
import com.claim.server.process.vo.SProcessVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/process")
public class ProcessApi {

    @Autowired
    private SProcessMainDao sProcessMainDao;

    @Autowired
    private SProcessNodeDao sProcessNodeDao;

    @RequestMapping(value = "getProcessList",method = RequestMethod.POST)
    public ApiResponse getProcessList(@RequestBody JSONObject params) {
        QueryWrapper<SProcessMain> wrapper = new QueryWrapper<>();
        wrapper.eq("ProcessType",params.getString("processType"));
        List<SProcessMain> list = sProcessMainDao.selectList(wrapper);
        List<SProcessVo> result = new ArrayList<>();
        for (SProcessMain processMain : list) {
            SProcessVo vo = new SProcessVo();
            Process process = new Process();
            process.setId(processMain.getId());
            process.setProcessType(processMain.getProcessType());
            process.setTitle(processMain.getTitle());
            if (StringUtils.isNotBlank(processMain.getInsurance())) {
                process.setInsurance(processMain.getInsurance().split(","));
            }
            process.setCreateTime(processMain.getCreateTime());
            process.setUpdateTime(processMain.getUpdateTime());
            vo.setProcess(process);
            QueryWrapper<SProcessNode> nwrapper = new QueryWrapper<>();
            nwrapper.eq("ProcessId",processMain.getId());
            List<SProcessNode> list2 = sProcessNodeDao.selectList(nwrapper);
            List<Node> nodes = new ArrayList<>();
            for (SProcessNode processNode : list2) {
                Node node = new Node();
                node.setId(processNode.getNodeId());
                node.setText(processNode.getText());
                node.setHandler(processNode.getHandler());
                node.setAmount(processNode.getAmount());
                nodes.add(node);
            }
            vo.setNodes(nodes);
            result.add(vo);
        }
        return new ApiResponse(result);
    }

    @RequestMapping(value = "saveProcess",method = RequestMethod.POST)
    public ApiResponse saveProcess(@RequestBody SProcessVo vo) {
        Process process = vo.getProcess();
        List<Node> nodes = vo.getNodes();

        SProcessMain processMain = new SProcessMain();
        processMain.setProcessType(process.getProcessType());
        processMain.setTitle(process.getTitle());
        if (process.getInsurance() != null && process.getInsurance().length > 0) {
            processMain.setInsurance(String.join(",",process.getInsurance()));
        } else {
            processMain.setInsurance(null);
        }

        if (process.getId() != null) {
            processMain.setId(process.getId());
            sProcessMainDao.updateById(processMain);
        } else {
            sProcessMainDao.insert(processMain);
        }

        sProcessNodeDao.deleteByProcessId(processMain.getId());
        for (Node node : nodes) {
            SProcessNode processNode = new SProcessNode();
            processNode.setProcessId(processMain.getId());
            processNode.setNodeId(node.getId());
            processNode.setText(node.getText());
            processNode.setHandler(node.getHandler());
            processNode.setAmount(node.getAmount());
            sProcessNodeDao.insert(processNode);
        }
        return new ApiResponse();
    }
}
