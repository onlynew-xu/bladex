package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceDataSmartElec;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DeviceDataSmartElecDao {
    /**
     * 获取最新的十条数据
     * @param deviceId
     * @return
     */
    List<DeviceDataSmartElec> getLastedTenData(@Param("deviceId") Long deviceId);

    DeviceDataSmartElec selectLastData(Long deviceId);

    List<Map<String, Object>> selectPowerVoltData(Long deviceId);

    List<Map<String, Object>> selectPowerAmpData(Long deviceId);

    List<Map<String, Object>> selectPowerFactorData(Long deviceId);

    List<Map<String, Object>> selectPowerActiveData(Long deviceId);

    List<Map<String, Object>> selectPowerReactiveData(Long deviceId);

    List<Map<String, Object>> selectPowerThdiData(Long deviceId);

    List<Map<String, Object>> selectPowerThdvData(Long deviceId);

    /**
     * 删除设备数据
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(@Param("deviceId") Long deviceId);


}