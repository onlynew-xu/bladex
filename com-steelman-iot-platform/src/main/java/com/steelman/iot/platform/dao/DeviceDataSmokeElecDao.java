package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceDataSmokeElec;
import org.apache.ibatis.annotations.Param;


public interface DeviceDataSmokeElecDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceDataSmokeElec record);

    int insertSelective(DeviceDataSmokeElec record);

    DeviceDataSmokeElec selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceDataSmokeElec record);

    int updateByPrimaryKey(DeviceDataSmokeElec record);

    DeviceDataSmokeElec selectBySbidLimit(Long deviceId);

    /**
     * 获取最新的烟雾数据
     * @param deviceId
     * @return
     */
    DeviceDataSmokeElec selectByLastedData(@Param("deviceId") Long deviceId);

    /**
     * 删除烟感数据
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(@Param("deviceId") Long deviceId);
}