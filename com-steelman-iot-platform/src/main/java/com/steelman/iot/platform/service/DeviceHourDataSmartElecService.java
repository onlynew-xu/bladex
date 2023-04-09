package com.steelman.iot.platform.service;

import com.steelman.iot.platform.energyManager.entity.DeviceHourDataSmartElec;

import java.util.List;

public interface DeviceHourDataSmartElecService {
    /**
     * 获取某日的电度数据
     * @param deviceId
     * @return
     */
    List<DeviceHourDataSmartElec> getDayData(Long deviceId,String yearMonthDay);
}
