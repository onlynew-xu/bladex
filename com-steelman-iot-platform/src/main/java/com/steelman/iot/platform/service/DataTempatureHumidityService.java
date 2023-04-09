package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.DataTemperaturehumidity;

public interface DataTempatureHumidityService {
    /**
     * 获取最新的温湿度数据
     * @param deviceId
     * @return
     */
      DataTemperaturehumidity getLastedData(Long deviceId);

    /**
     * 删除设备数据
     * @param deviceId
     * @return
     */
     int deleteByDeviceId(Long deviceId);
}
