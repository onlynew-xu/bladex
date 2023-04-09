package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceDataCompensateElec;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceDataCompensateElecDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceDataCompensateElec record);

    int insertSelective(DeviceDataCompensateElec record);

    DeviceDataCompensateElec selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceDataCompensateElec record);

    int updateByPrimaryKey(DeviceDataCompensateElec record);

    /**
     * 获取最新的十条数据
     * @param deviceId
     * @return
     */
    List<DeviceDataCompensateElec> getLastedTenData(@Param("deviceId") Long deviceId);

    /**
     * 删除设备数据
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(@Param("deviceId") Long deviceId);
}