package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceParamsWaveElec;
import org.apache.ibatis.annotations.Param;

public interface DeviceParamsWaveElecDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceParamsWaveElec record);

    int insertSelective(DeviceParamsWaveElec record);

    DeviceParamsWaveElec selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceParamsWaveElec record);

    int updateByPrimaryKey(DeviceParamsWaveElec record);

    void updateVariableParams(DeviceParamsWaveElec deviceParamsWaveElec);

    DeviceParamsWaveElec findByDeviceId(Long deviceId);

    /**
     * 删除设备参数
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(@Param("deviceId") Long deviceId);
}