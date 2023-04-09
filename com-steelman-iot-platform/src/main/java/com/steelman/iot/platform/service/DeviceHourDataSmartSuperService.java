package com.steelman.iot.platform.service;

import com.steelman.iot.platform.energyManager.entity.DeviceHourDataSmartSuper;

import java.util.List;

public interface DeviceHourDataSmartSuperService {

    public List<DeviceHourDataSmartSuper> getDayData(Long deviceId,String yearMonthDay);
}
