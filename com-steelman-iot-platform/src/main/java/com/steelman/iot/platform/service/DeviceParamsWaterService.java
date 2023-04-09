package com.steelman.iot.platform.service;
import com.steelman.iot.platform.entity.DeviceParamsWater;

public interface DeviceParamsWaterService {

    void insertSelective(DeviceParamsWater record);
    void update(DeviceParamsWater record);
    void deleteById(Long id);
    DeviceParamsWater findByid(Long id);
    DeviceParamsWater findByDeviceId(Long deviceId);

    void updateVariableParams(DeviceParamsWater variableParamsWater);

    /**
     * 删除设备参数
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(Long deviceId);
}
