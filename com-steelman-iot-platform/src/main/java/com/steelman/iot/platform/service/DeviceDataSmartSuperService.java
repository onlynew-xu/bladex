package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.DeviceDataSmartElec;
import com.steelman.iot.platform.entity.DeviceDataSmartSuper;

import java.util.List;

public interface DeviceDataSmartSuperService {

    List<DeviceDataSmartSuper> getLastedTenData(Long id);

    int deleteByDeviceId(Long deviceId);

    /**
     * 获取设备的最新一条数据
     * @param deviceId
     * @return
     */
    DeviceDataSmartSuper getLastData(Long deviceId);

    /**
     * 获取最新的十条
     * @param deviceId
     * @return
     */
    List<DeviceDataSmartElec> getLastedTenDataElec(Long deviceId);
}
