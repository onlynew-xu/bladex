package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.DeviceDataDoorElec;

public interface DeviceDataDoorElecService {
    /**
     * 获取最新的数据
     * @param deviceId
     * @return
     */
    DeviceDataDoorElec selectByLastedData(Long deviceId);

    /**
     * 删除设备数据
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(Long deviceId);
}
