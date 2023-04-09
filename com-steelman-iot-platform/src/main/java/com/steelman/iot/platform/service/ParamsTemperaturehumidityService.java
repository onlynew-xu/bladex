package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.DeviceParamsTemperaturehumidity;

public interface ParamsTemperaturehumidityService {

    Integer insertRecord(DeviceParamsTemperaturehumidity deviceParamsTemperaturehumidity);

    /**
     * 修改设备参数
     * @param deviceParamsTemperaturehumidity
     */
    void updateVariableParams(DeviceParamsTemperaturehumidity deviceParamsTemperaturehumidity);

    DeviceParamsTemperaturehumidity findByDeviceId(Long deviceId);

    /**
     * 删除设备参数
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(Long deviceId);
}
