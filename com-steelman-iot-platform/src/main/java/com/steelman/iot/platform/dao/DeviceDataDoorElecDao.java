package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceDataDoorElec;
import org.apache.ibatis.annotations.Param;

public interface DeviceDataDoorElecDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceDataDoorElec record);

    int insertSelective(DeviceDataDoorElec record);

    DeviceDataDoorElec selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceDataDoorElec record);

    int updateByPrimaryKey(DeviceDataDoorElec record);

    /**
     * 获取最新的 防火门数据
     * @param deviceId
     * @return
     */
    DeviceDataDoorElec selectByLastedData(@Param("deviceId") Long deviceId);

    /**
     * 删除设备数据
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(@Param("deviceId") Long deviceId);
}