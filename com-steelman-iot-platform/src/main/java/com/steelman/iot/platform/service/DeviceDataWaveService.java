package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.DeviceDataWaveElec;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceDataWaveService {
    /**
     * 获取最新的十条数据
     * @param deviceId
     * @return
     */
    List<DeviceDataWaveElec>  getLastedTenData(Long deviceId);

    /**
     * 删除设备数据
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(@Param("deviceId") Long deviceId);
}
