package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.FactoryInfo;

public interface FactoryInfoService {

    Integer registerCityFactoryInfo(Long projectId);

    Integer configureFactoryInfo(Long projectId);

    FactoryInfo findByProjectId(Long projectId);
}
