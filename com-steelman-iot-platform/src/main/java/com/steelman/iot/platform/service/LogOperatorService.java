package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.LogOperator;
import com.steelman.iot.platform.entity.dto.LogOperatorDto;

import java.util.List;

public interface LogOperatorService {

    Integer saveLog(LogOperator logOperator);

    List<LogOperator> getLogOperate(Long userId);

    List<LogOperatorDto> getLogOperateByProjectId(Long projectId);
}
