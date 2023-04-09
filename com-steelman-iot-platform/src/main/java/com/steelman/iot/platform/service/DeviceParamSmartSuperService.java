package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.DeviceParamSmartSuper;

public interface DeviceParamSmartSuperService {

    int saveParam(DeviceParamSmartSuper deviceParamSmartSuper);

    int deleteByDeviceId(Long deviceId);

    DeviceParamSmartSuper  findByDeviceId(Long id);

    int updateVariableParams(DeviceParamSmartSuper deviceParamSmartSuper);
}
