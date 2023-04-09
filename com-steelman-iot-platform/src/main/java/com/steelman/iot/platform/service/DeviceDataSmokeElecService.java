package com.steelman.iot.platform.service;
import com.steelman.iot.platform.entity.DeviceDataSmokeElec;

public interface DeviceDataSmokeElecService {

    void insertSelective(DeviceDataSmokeElec record);
    void update(DeviceDataSmokeElec record);
    void deleteById(Long id);
    DeviceDataSmokeElec findByid(Long id);

    DeviceDataSmokeElec selectBySbidLimit(Long deviceId);

    DeviceDataSmokeElec selectByLastedData(Long deviceId);

    /**
     * 删除设备数据
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(Long deviceId);
}
