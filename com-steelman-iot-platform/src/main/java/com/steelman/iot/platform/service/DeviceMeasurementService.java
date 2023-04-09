package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.DeviceMeasurement;

import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/4/15 0015 11:25
 * @Description:
 */
public interface DeviceMeasurementService {
    List<Map<String, Object>> getLastSevenDayTotal(Long deviceId);

    /**
     * 删除设备的电度数据
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(Long deviceId);

    List<DeviceMeasurement> getMeasureList(String serialNum,String date);

    int updateData(List<DeviceMeasurement> deviceMeasurementList);

    DeviceMeasurement getLastDevice(Long deviceId);
}
