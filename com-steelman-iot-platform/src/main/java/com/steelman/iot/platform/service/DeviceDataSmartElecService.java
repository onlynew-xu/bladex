package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.DeviceDataSmartElec;

import java.util.List;
import java.util.Map;

public interface DeviceDataSmartElecService {


    /**
     * 获取最新的十条数据 用于折线图
     * @param deviceId
     * @return
     */
    List<DeviceDataSmartElec> getLastedTenData(Long deviceId);

    DeviceDataSmartElec getLastData(Long deviceId);

    List<Map<String, Object>> selectPowerVoltData(Long deviceId);

    List<Map<String, Object>> selectPowerAmpData(Long deviceId);

    List<Map<String, Object>> selectPowerFactorData(Long deviceId);

    List<Map<String, Object>> selectPowerActiveData(Long deviceId);

    List<Map<String, Object>> selectPowerReactiveData(Long deviceId);

    List<Map<String, Object>> selectThdiData(Long deviceId);

    List<Map<String, Object>> selectThdvData(Long deviceId);

    /**
     * 删除设备数据
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(Long deviceId);

}
