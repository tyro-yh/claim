package com.claim.server.process.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.process.po.SProcessNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SProcessNodeDao extends BaseMapper<SProcessNode> {

    void deleteByProcessId(@Param(value = "processId") Integer processId);

    List<SProcessNode> selectByProcessId(@Param(value = "processId") Integer processId);

    SProcessNode selectByProcessIdAndNodeId(@Param(value = "processId") Integer processId,@Param(value = "nodeId") String nodeId);
}
