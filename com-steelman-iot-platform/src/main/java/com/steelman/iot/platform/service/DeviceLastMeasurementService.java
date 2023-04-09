package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.DeviceMeasurement;

import java.util.List;

public interface DeviceLastMeasurementService {

    List<DeviceMeasurement> getMeasureData(String[] pastDateStrArr, Long deviceId);
}
