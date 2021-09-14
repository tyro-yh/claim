package com.claim.server.process.service.impl;

import com.claim.server.process.dao.SProcessMainDao;
import com.claim.server.process.dao.SProcessNodeDao;
import com.claim.server.process.po.SProcessMain;
import com.claim.server.process.po.SProcessNode;
import com.claim.server.process.service.ProcessService;
import com.claim.server.utils.TaskTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private SProcessMainDao sProcessMainDao;

    @Autowired
    private SProcessNodeDao sProcessNodeDao;

    @Override
    public String getHandler(TaskTypeEnum taskTypem, String insuranceCode) {
        //获取配置的审核流程
        SProcessMain processMain = sProcessMainDao.selectLastProcess(taskTypem.getCode(),insuranceCode);
        if (processMain == null) {
            //不存在针对险种的特殊配置则获取通用配置
            processMain = sProcessMainDao.selectCommonProcess(taskTypem.getCode());
        }

        String handler = null;
        if (processMain != null) {
            List<SProcessNode> nodes = sProcessNodeDao.selectByProcessId(processMain.getId());
            if (!nodes.isEmpty()) {
                //获取第一个审核节点
                SProcessNode node = nodes.get(0);
                handler = node.getHandler();
            }
        }

        return handler;
    }
}
