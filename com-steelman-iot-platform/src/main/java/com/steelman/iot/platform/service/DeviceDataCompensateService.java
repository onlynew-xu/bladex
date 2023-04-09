package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.DeviceDataCompensateElec;

import java.util.List;

public interface DeviceDataCompensateService {

    List<DeviceDataCompensateElec> getLastedTenData(Long deviceId);

    /**
     * 删除设备数据
      * @param deviceId
     * @return
     */
    int deleteByDeviceId(Long deviceId);
}
