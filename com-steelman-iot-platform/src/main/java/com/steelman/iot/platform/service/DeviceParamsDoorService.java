package com.steelman.iot.platform.service;
import com.steelman.iot.platform.entity.DeviceParamsDoor;
public interface DeviceParamsDoorService {

    void insertSelective(DeviceParamsDoor record);
    void update(DeviceParamsDoor record);
    void deleteById(Long id);
    DeviceParamsDoor findByid(Long id);
    DeviceParamsDoor findByDeviceId(Long deviceId);

    /**
     * 删除设备参数
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(Long deviceId);

    int updateVariableParams(DeviceParamsDoor deviceParamsDoor);
}
