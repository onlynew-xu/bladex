package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.FactoryProcessUnit;

public interface FactoryProcessUnitService {

    FactoryProcessUnit getByFactoryId(Long factoryInfoId);
}
