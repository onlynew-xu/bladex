package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceDataWaveElec;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceDataWaveElecDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceDataWaveElec record);

    int insertSelective(DeviceDataWaveElec record);

    DeviceDataWaveElec selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceDataWaveElec record);

    int updateByPrimaryKey(DeviceDataWaveElec record);

    /**
     * 获取最新的十条数据
     * @param deviceId
     * @return
     */
    List<DeviceDataWaveElec> getLastedTenData(@Param("deviceId") Long deviceId);

    /**
     * 删除设备数据
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(@Param("deviceId") Long deviceId);
}