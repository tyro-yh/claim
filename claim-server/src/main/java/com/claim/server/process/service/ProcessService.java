package com.claim.server.process.service;

import com.claim.server.utils.TaskTypeEnum;

public interface ProcessService {

    String getHandler(TaskTypeEnum taskTypem,String insuranceCode);
}
