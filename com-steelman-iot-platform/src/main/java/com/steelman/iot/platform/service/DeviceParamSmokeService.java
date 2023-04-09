package com.steelman.iot.platform.service;
import com.steelman.iot.platform.entity.DeviceParamsSmoke;

public interface DeviceParamSmokeService {

    void insertSelective(DeviceParamsSmoke record);
    void update(DeviceParamsSmoke record);
    void deleteById(Long id);
    DeviceParamsSmoke findByid(Long id);
    DeviceParamsSmoke findByDeviceId(Long deviceId);

    int updateVariableParams(DeviceParamsSmoke variableParamsSmoke);

    /**
     * 删除设备参数
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(Long deviceId);
}
