package com.steelman.iot.platform.service;
import com.steelman.iot.platform.entity.DeviceDataSafeElec;

import java.util.List;

/**
 * @author tang
 * date 2020-11-23
 */
public interface DeviceDataSafeElecService {

    void insertSelective(DeviceDataSafeElec record);
    void update(DeviceDataSafeElec record);
    void deleteById(Long id);
    DeviceDataSafeElec findByid(Long id);
    List<DeviceDataSafeElec> selectBySbidLimit(Long deviceId);

    /**
     * 删除设备数据
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(Long deviceId);
}
