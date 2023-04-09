package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceDataWaterElec;
import org.apache.ibatis.annotations.Param;

public interface DeviceDataWaterElecDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceDataWaterElec record);

    int insertSelective(DeviceDataWaterElec record);

    DeviceDataWaterElec selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceDataWaterElec record);

    int updateByPrimaryKey(DeviceDataWaterElec record);

    /**
     * 获取最新的水压数据
     * @param deviceId
     * @return
     */
    DeviceDataWaterElec findRecentData(@Param("deviceId") Long deviceId);

    /**
     * 删除设备数据
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(@Param("deviceId") Long deviceId);
}