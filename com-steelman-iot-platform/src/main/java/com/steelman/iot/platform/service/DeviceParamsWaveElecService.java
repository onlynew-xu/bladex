package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.DeviceParamsWaveElec;

public interface DeviceParamsWaveElecService {

    int insertRecord(DeviceParamsWaveElec deviceParamsWaveElec);

    void updateVariableParams(DeviceParamsWaveElec deviceParamsWaveElec);

    DeviceParamsWaveElec findByDeviceId(Long deviceId);

    /**
     * 删除设备参数
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(Long deviceId);
}
