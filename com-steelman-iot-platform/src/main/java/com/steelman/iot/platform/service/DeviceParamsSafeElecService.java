package com.steelman.iot.platform.service;
import com.steelman.iot.platform.entity.DeviceParamsSafeElec;

public interface DeviceParamsSafeElecService {

    void insertSelective(DeviceParamsSafeElec record);
    void update(DeviceParamsSafeElec record);
    void deleteById(Long id);
    DeviceParamsSafeElec findByid(Long id);
    DeviceParamsSafeElec findByDeviceId(Long deviceId);

    /**
     * 修改设备参数
     * @param variableSafeParams
     * @return
     */
    int updateVariableParams(DeviceParamsSafeElec variableSafeParams);

    /**
     * 删除设备的参数数据
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(Long deviceId);
}
