package com.claim.server.datacollection.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claim.server.datacollection.po.BFileList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BFileListDao extends BaseMapper<BFileList> {

    List<BFileList> selectByCollectionId(@Param(value = "collectionId") Integer collectionId);
}
